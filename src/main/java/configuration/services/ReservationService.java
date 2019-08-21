package configuration.services;


import configuration.daos.ReservationDao;
import configuration.errors.exceptions.CreationException;
import configuration.errors.exceptions.ResourceCreationException;
import configuration.response.ReservationResponseItem;

import javax.inject.Inject;
import java.util.Optional;

public class ReservationService {

    @Inject
    ReservationDao reservationDao;

    public ReservationResponseItem reserve(String tripId, String userId, Integer quantity) {

        if (tripId == null || userId == null || quantity == null) {
            throw new ResourceCreationException(CreationException.BODY_PARAMS_NULL);
        }

        Optional<ReservationResponseItem> reservation = reservationDao.reserve(tripId, userId, quantity);

        return reservation.orElseThrow(ResourceCreationException::new);

    }

}
