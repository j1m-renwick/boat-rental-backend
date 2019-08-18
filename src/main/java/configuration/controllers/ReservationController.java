package configuration.controllers;

import configuration.daos.RedisDao;
import configuration.response.Harbour;
import configuration.response.ReservationResponseItem;
import configuration.response.TripResponseItem;
import configuration.services.TripService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;

import javax.annotation.Nullable;
import javax.inject.Inject;

@Controller("/reservations")
public class ReservationController {


/*  generates token and calls POST /reservations/reserve?quantity=XXX&tripId=<trip_id>
-> maintains cache of ticket number per trip and active reservation ids with quantities attached
-> if quantity requested available, returns reservation id and expiry dttm (UTC)

-> POST /reservations/reserve?quantity=XXX&tripId=<trip_id>&userId=<userId>
-> GET /reservations/<reservation_id>?userId=<userId>
-> GET /trips/<trip_id>/reservations?userId=<userId> ?????
 */

/*
    generates token and calls POST /reservations/<reservation_id>/purchase
-> if reservation id exists in cache and is before expiry dttm, call to DB to obtain required quantity of free tickets and update them with purchase info (or relational table for purchasing user details)
-> returns ticket ids and/or link to ticket resources (or error and clear cache entry if quantity not available in DB)
 */

    @Inject
    private TripService tripService;

    @Inject
    private RedisDao<TripResponseItem> redisDao;


    @Post("/reserve")
    public ReservationResponseItem reserveTickets(@QueryValue String tripId,
                                                  @QueryValue String userId,
                                                  @Nullable @QueryValue("1") Integer quantity) {

        TripResponseItem item = new TripResponseItem();
        item.setHarbour(Harbour.VICTORIA_HARBOUR);

        System.out.println(redisDao.put("foo", item));

        System.out.println(redisDao.get("foo", TripResponseItem.class));

//         check that entry for userId doesn't already exist - if so, fail

        if (redisDao.get(userId) != null) {
//             check that the number of reserved tickets + quantity isn't above the total on sale
        } else {
            System.out.println("key isn't null");
            // fail
        }


        return null;
    }

    @Get("/{reservationId}")
    public ReservationResponseItem getTicket(String reservationId, @QueryValue String userId) {
        return null;
    }

}