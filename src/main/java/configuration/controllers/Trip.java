package configuration.controllers;

import configuration.daos.TripDao;
import configuration.errors.exceptions.ResourceNotFoundException;
import configuration.request.TripCreateRequest;
import configuration.response.CreatedResponse;
import configuration.response.ResponseWrapper;
import configuration.response.TripResponseItem;
import configuration.services.LinkService;
import io.micronaut.core.convert.format.Format;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Set;

@Controller("/trips")
public class Trip {

    @Inject
    private TripDao tripDao;

    @Inject
    private LinkService linkService;

    @Post
    public CreatedResponse addTrip(TripCreateRequest requestBody) {
        return new CreatedResponse(tripDao.addTrip(requestBody));

    }

    @Get("/{tripId}")
    public TripResponseItem getTrip(String tripId) {
        return tripDao.getTrip(tripId);
    }

    // Optional LocalDate should be in 1.1.5 milestone release (https://github.com/micronaut-projects/micronaut-core/issues/1916)
    @Get
    public ResponseWrapper<TripResponseItem> getTrips(@QueryValue(defaultValue = "0") Integer offset,
                                                      @QueryValue(defaultValue = "1") Integer limit,
                                                      @QueryValue @Nullable @Format("yyyy-MM-dd") LocalDate date) {

        ResponseWrapper<TripResponseItem> response = new ResponseWrapper<>();
        Set<TripResponseItem> tripResults = tripDao.getTrips(offset, limit, date);
        if (tripResults.size() == 0) {
            throw new ResourceNotFoundException();
        }
        if (tripDao.getTotalCount(date) > (offset + limit)) {
            response.setNext(linkService.getNextLink(offset, limit));
        }
        if ((offset > 0)) {
            response.setPrevious(linkService.getPreviousLink(offset, limit));
        }
        response.setResources(tripResults);
        return response;
    }
}