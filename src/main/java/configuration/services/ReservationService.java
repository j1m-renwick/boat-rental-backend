package configuration.services;


import configuration.daos.ReservationDao;
import configuration.errors.exceptions.ResourceCreationException;
import configuration.response.ReservationResponseItem;

import javax.inject.Inject;
import java.util.Optional;

public class ReservationService {

    @Inject
    ReservationDao reservationDao;

    public ReservationResponseItem reserve(String tripId, String userId, Integer quantity) {

        reservationDao.clearExpiredReservations();

        Optional<ReservationResponseItem> reservation = reservationDao.reserve(tripId, userId, quantity);

        return reservation.orElseThrow(ResourceCreationException::new);

    }

}
