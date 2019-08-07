package configuration.daos;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import configuration.request.TripCreateRequest;
import configuration.response.TripResponseItem;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class TripDao {


    private static final Logger log = LoggerFactory.getLogger(TripDao.class);

    private static final PojoChecker checker = PojoChecker.of(TripResponseItem.class);

    private static final String TRIP_DATE_TIME_FIELD = "departureDttm";

    @Inject
    private MongoClient mongoClient;

    private MongoCollection<TripResponseItem> collection;

    @PostConstruct
    private void initialise() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        collection = mongoClient.getDatabase("junk").withCodecRegistry(pojoCodecRegistry).getCollection("trips", TripResponseItem.class);

        checker.check(TRIP_DATE_TIME_FIELD);
    }

    // TODO FIX THESE 2 METHODS


    public String addTrip(TripCreateRequest requestBody) {

        TripResponseItem item = new TripResponseItem();
        item.setDepartureDttm(LocalDateTime.now());
        collection.insertOne(item);
        return "something";

//        try {
//            log.info("attempting to add trip: " + objectMapper.writeValueAsString(requestBody));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        // reject if multiple trips happen with the same junk on the same day
//        if (collection.find(and(eq("junkId", requestBody.getJunkId()),
//                eq("date", requestBody.getDate()))).first() != null) {
//            log.info(String.format("failed - entry already exists on %s for company code: %s",
//                    requestBody.getJunkId(), requestBody.getDate()));
//            throw new ResourceAlreadyExistsException();
//        }
//        Document doc = objectMapper.convertValue(requestBody, Document.class);
//        collection.insertOne(doc);
//        log.info("trip added successfully, assigned DB id: " + doc.get("_id").toString());
//        return doc.get("_id").toString();
    }

    public TripResponseItem getTrip(String tripId) {

//        Document doc = collection.find(eq("_id", new ObjectId(tripId))).first();
//        if (doc != null) {
//            return objectMapper.convertValue(doc, TripResponseItem.class);
//        } else {
//            throw new ResourceNotFoundException();
//        }
        return null;

    }

    public Set<TripResponseItem> getTrips(Integer offset,
                                          Integer limit,
                                          LocalDate date) {

        FindIterable<TripResponseItem> foundItems = date != null ? collection.find(onDay(TRIP_DATE_TIME_FIELD, date)) : collection.find();

        Set<TripResponseItem> ret = new HashSet<>();
        foundItems.skip(offset).limit(limit).forEach((Consumer<? super TripResponseItem>) ret::add);
        return ret;

    }

    public Long getTotalCount(LocalDate date) {
        return date != null ? collection.countDocuments(onDay(TRIP_DATE_TIME_FIELD, date)) : collection.countDocuments();
    }



    // helper methods

    private Bson inRange(String fieldName, LocalDateTime startDate, LocalDateTime endDate) {
        return and(gte(fieldName, startDate), lt(fieldName, endDate));

    }

    private Bson onDay(String fieldName, LocalDate day) {
        return inRange(fieldName, day.atStartOfDay(), day.plusDays(1L).atStartOfDay());
    }

}
