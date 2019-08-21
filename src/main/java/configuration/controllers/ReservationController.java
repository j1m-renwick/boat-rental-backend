package configuration.controllers;

import configuration.response.ReservationResponseItem;
import configuration.services.ReservationService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;

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
    private ReservationService reservationService;



    @Post("/reserve")
    @Status(HttpStatus.CREATED)
    public ReservationResponseItem reserveTickets(@QueryValue String tripId,
                                                  @QueryValue String userId,
                                                  @Nullable @QueryValue(defaultValue = "1") Integer quantity) {

        return reservationService.reserve(tripId, userId, quantity);

    }

    @Get("/{userId}")
    public String getTicket(String userId) {
        return "hello!";
    }

}