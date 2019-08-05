package configuration.service.daos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import configuration.service.errors.exceptions.ResourceAlreadyExistsException;
import configuration.service.errors.exceptions.ResourceNotFoundException;
import configuration.service.request.CompanyCreateRequest;
import configuration.service.request.TripCreateRequest;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class TripDao {


    private static final Logger log = LoggerFactory.getLogger(TripDao.class);

    @Inject
    ObjectMapper objectMapper;

    @Inject
    private MongoClient mongoClient;

    private MongoCollection<Document> collection;

    @PostConstruct
    private void initialise() {
        collection = mongoClient.getDatabase("test").getCollection("trips");
    }


    public String addTrip(TripCreateRequest requestBody) {

        try {
            log.info("attempting to add trip: " + objectMapper.writeValueAsString(requestBody));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // reject if multiple trips happen with the same junk on the same day
        if (collection.find(and(eq("junkId", requestBody.getJunkId()),
                eq("date", requestBody.getDate()))).first() != null) {
            log.info(String.format("failed - entry already exists on %s for company code: %s",
                    requestBody.getJunkId(), requestBody.getDate()));
            throw new ResourceAlreadyExistsException();
        }
        Document doc = objectMapper.convertValue(requestBody, Document.class);
        collection.insertOne(doc);
        log.info("trip added successfully, assigned DB id: " + doc.get("_id").toString());
        return doc.get("_id").toString();
    }

    public TripCreateRequest getTrip(String tripId) {

        Document doc = collection.find(eq("_id", new ObjectId(tripId))).first();
        if (doc != null) {
            return objectMapper.convertValue(doc, TripCreateRequest.class);
        } else {
            throw new ResourceNotFoundException();
        }

    }

}
