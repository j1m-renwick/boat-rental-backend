package configuration.services;


import configuration.daos.TripDao;
import configuration.errors.exceptions.ResourceNotFoundException;
import configuration.request.TripCreateRequest;
import configuration.response.CreatedResponse;
import configuration.response.Harbour;
import configuration.response.ResponseWrapper;
import configuration.response.TripResponseItem;
import configuration.response.TripType;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Set;

public class TripService {

    @Inject
    private TripDao tripDao;

    @Inject
    private LinkService linkService;

    public CreatedResponse addTrip(TripCreateRequest requestBody) {
        return new CreatedResponse(tripDao.addTrip(requestBody));

    }

    public TripResponseItem getTrip(String tripId) {
        return tripDao.getTrip(tripId);
    }

    public ResponseWrapper<TripResponseItem> getTrips(Integer offset,
                                                      Integer limit,
                                                      LocalDate date,
                                                      Harbour harbour,
                                                      TripType type) {

        ResponseWrapper<TripResponseItem> response = new ResponseWrapper<>();
        Set<TripResponseItem> tripResults = tripDao.getTrips(offset, limit, date, harbour, type);
        if (tripResults.size() == 0) {
            throw new ResourceNotFoundException();
        }
        if (tripDao.getTotalCount(date, harbour, type) > (offset + limit)) {
            response.setNext(linkService.getNextLink(offset, limit));
        }
        if ((offset > 0)) {
            response.setPrevious(linkService.getPreviousLink(offset, limit));
        }
        response.setResources(tripResults);
        return response;

    }

}
