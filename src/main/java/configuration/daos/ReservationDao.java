package configuration.daos;

import configuration.errors.exceptions.CreationException;
import configuration.errors.exceptions.ResourceCreationException;
import configuration.response.ReservationResponseItem;
import io.lettuce.core.Range;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.micronaut.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class ReservationDao {

    private Logger log = LoggerFactory.getLogger(ReservationDao.class);

    @Inject
    private StatefulRedisConnection<String, String> connection;

    @Inject
    private TripDao tripDao;

    private RedisCommands<String, String> commands;

    private final static String RESERVATION_SET = "reservationSet";
    private final static String TRIP_ID = "tripId";
    private final static String QUANTITY = "quantity";

    // one minute reservation expiry
    private final static long EXPIRY_TIME_MS = 60_000;

    @PostConstruct
    private void initialise() {
        commands = connection.sync();
        // TODO only populate if not in cache already - other instances might be up
        commands.flushdb();
        for (Map.Entry<String, Integer> entry : tripDao.getAllQuantities().entrySet()) {
            commands.set(entry.getKey(), entry.getValue().toString());
        }
    }

    /**
     * clears any expired reservations from the redis reservation set.
     */
    @Scheduled(fixedDelay = "1m")
    public void clearExpiredReservations() {
        log.info("**CLEARING EXPIRED RESERVATIONS**");

        Range<Long> range = Range.unbounded();
        range.lt(System.currentTimeMillis());

        List<String> itemsToRemove = commands.zrangebyscore(RESERVATION_SET, range);

        for (String item : itemsToRemove) {

            Runnable runnable = (() -> {
                commands.watch(item);
                String itemTripId = commands.hget(item, TRIP_ID);
                String itemQty = commands.hget(item, QUANTITY);
                commands.multi();
                commands.incrby(itemTripId, Long.parseLong(itemQty));
                commands.zrem(RESERVATION_SET, item);
                commands.del(item);
            });

            log.info(String.format("clearance of expired reservation %s - %s", item, RedisUtility.execWithRetries(commands, runnable) != null ? "SUCCESS!" : "FAILED!"));
        }

        log.info("**EXPIRED CLEARING COMPLETE**");
    }


    private Optional<ReservationResponseItem> getReservation(String userId) {
        Map<String, String> toReturn = commands.hgetall(userId);

        if (toReturn.isEmpty()) {
            return Optional.empty();
        }

        ReservationResponseItem reservationResponseItem = new ReservationResponseItem();
        reservationResponseItem.setUserId(userId);
        reservationResponseItem.setTripId(toReturn.get(TRIP_ID));
        reservationResponseItem.setTicketQuantity(Integer.parseInt(toReturn.get(QUANTITY)));
        reservationResponseItem.setExpiryDttm(LocalDateTime.ofInstant(Instant.ofEpochMilli(commands.zscore(RESERVATION_SET, userId).longValue()), ZoneId.systemDefault()));
        return Optional.of(reservationResponseItem);
    }

    /**
     * Clears an existing reservation and returns the reserved quantity to the appropriate trip ticket pool
     *
     * @param userId the userId to clear the reservation for
     * @return true if the reservation was cleared successfully - else returns false
     */
    // TODO instead of empty optionals, throw exceptions with detail and log them
    private boolean clearReservation(String userId) {
        Map<String, String> reservation = commands.hgetall(userId);

        log.info("existing entry overwritten:" + !reservation.isEmpty());
        if (reservation.isEmpty()) {
            return true;
        }

        Runnable runnable = (() -> {
            commands.multi();
            commands.incrby(reservation.get(TRIP_ID), Long.parseLong(reservation.get(QUANTITY)));
            commands.del(userId);
        });

        return RedisUtility.execWithRetries(commands, runnable) != null;

    }

    private Optional<ReservationResponseItem> saveReservation(String userId, String tripId, Integer reserveQuantity) {

        if (!clearReservation(userId)) {
            return Optional.empty();
        }

        // create hash entry in redis with a key of the userId
        Map<String, String> map = new HashMap<>();
        map.put(TRIP_ID, tripId);
        map.put(QUANTITY, reserveQuantity.toString());

        long expiryTime = System.currentTimeMillis() + EXPIRY_TIME_MS;

        Runnable runnable = (() -> {
            commands.multi();
            commands.decrby(tripId, reserveQuantity);
            // create hash entry in redis with a key of the userId
            commands.hmset(userId, map);
            // add the userId to the sorted set
            commands.zadd(RESERVATION_SET, expiryTime, userId);
        });

        TransactionResult result = RedisUtility.execWithRetries(commands, runnable);

        if (result == null) {
            return Optional.empty();
        }

        log.info("post-reserve quantity: " + result.get(0));
        log.info("created reservation: " + map.toString());

        ReservationResponseItem reservationResponseItem = new ReservationResponseItem();
        reservationResponseItem.setUserId(userId);
        reservationResponseItem.setTripId(tripId);
        reservationResponseItem.setTicketQuantity(reserveQuantity);
        reservationResponseItem.setExpiryDttm(LocalDateTime.ofInstant(Instant.ofEpochMilli(expiryTime), ZoneId.systemDefault()));
        return Optional.of(reservationResponseItem);
    }

    public Optional<ReservationResponseItem> reserve(String tripId, String userId, Integer quantity) {

        // TODO make this a user specific out-of-date clearance
        clearExpiredReservations();

        log.info("USER ID:" + userId);
        log.info("TRIP ID:" + tripId);

        commands.watch(tripId, userId);

        int totalQuantity = Integer.parseInt(commands.get(tripId));
        log.info("pre-reserve quantity: " + totalQuantity);

        String existingQuantity = commands.hget(userId, QUANTITY);
        String existingTripId = commands.hget(userId, TRIP_ID);
        boolean differentTripId = existingTripId == null || !existingTripId.equals(tripId);
        int existingQty = existingQuantity != null ? Integer.parseInt(existingQuantity) : 0;
        int qtyDiff = quantity - existingQty;

        // only create a new entry if there are enough tickets and there is a different quantity number being reserved from the existing entry
        if (differentTripId) {
            return saveReservation(userId, tripId, quantity);
        } else {
            if (qtyDiff <= totalQuantity) {
                if (qtyDiff != 0) {
                    return saveReservation(userId, tripId, quantity);
                } else {
                    log.info("returning existing entry");
                    return getReservation(userId);
                }
            } else {
                throw new ResourceCreationException(CreationException.QUANTITY_TOO_LARGE);
            }
        }
    }
}
