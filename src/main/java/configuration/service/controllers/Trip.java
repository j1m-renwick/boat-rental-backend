package configuration.service.controllers;

import configuration.service.daos.TripDao;
import configuration.service.request.TripCreateRequest;
import configuration.service.response.CreatedResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;

@Controller("/trip")
public class Trip {

    @Inject
    private TripDao tripDao;

    // TODO add all CRUD

    @Post()
    public CreatedResponse addTrip(TripCreateRequest requestBody) {
        return new CreatedResponse(tripDao.addTrip(requestBody));

    }

    @Get("/{tripId}")
    public TripCreateRequest getTrip(String tripId) {
        return tripDao.getTrip(tripId);
    }
}