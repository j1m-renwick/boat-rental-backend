package configuration.controllers;

import configuration.request.TripCreateRequest;
import configuration.response.CreatedResponse;
import configuration.response.Harbour;
import configuration.response.ResponseWrapper;
import configuration.response.TripResponseItem;
import configuration.response.TripType;
import configuration.services.TripService;
import io.micronaut.core.convert.format.Format;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.time.LocalDate;

@Controller("/reservation")
public class ReservationController {


/*  generates token and calls POST /reservations/reserve?quantity=XXX&tripId=<trip_id>
-> maintains cache of ticket number per trip and active reservation ids with quantities attached
-> if quantity requested available, returns reservation id and expiry dttm (UTC)



-> POST /reservations/reserve?quantity=XXX&tripId=<trip_id>
-> GET /reservations/<reservation_id>
-> GET /trips/<trip_id>/reservations
 */

/*
    generates token and calls POST /reservations/<reservation_id>/purchase
-> if reservation id exists in cache and is before expiry dttm, call to DB to obtain required quantity of free tickets and update them with purchase info (or relational table for purchasing user details)
-> returns ticket ids and/or link to ticket resources (or error and clear cache entry if quantity not available in DB)
 */

    @Inject
    private TripService tripService;

    @Post
    public CreatedResponse addTrip(TripCreateRequest requestBody) {
        return tripService.addTrip(requestBody);

    }

    @Get("/{tripId}")
    public TripResponseItem getTrip(String tripId) {
        return tripService.getTrip(tripId);
    }

    // Optional LocalDate should be in 1.1.5 milestone release (https://github.com/micronaut-projects/micronaut-core/issues/1916)
    @Get
    public ResponseWrapper<TripResponseItem> getTrips(@QueryValue(defaultValue = "0") Integer offset,
                                                      @QueryValue(defaultValue = "1") Integer limit,
                                                      @QueryValue @Nullable @Format("yyyy-MM-dd") LocalDate date,
                                                      @QueryValue @Nullable Harbour harbour,
                                                      @QueryValue @Nullable TripType type) {

        return tripService.getTrips(offset, limit, date, harbour, type);

    }
}