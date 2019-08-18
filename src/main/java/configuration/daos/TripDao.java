package configuration.daos;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import configuration.request.TripCreateRequest;
import configuration.response.Harbour;
import configuration.response.TripResponseItem;
import configuration.response.TripType;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Sorts.ascending;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

//import com.mongodb.reactivestreams.client.MongoClient;
//import com.mongodb.reactivestreams.client.MongoClients;
//import com.mongodb.reactivestreams.client.MongoCollection;

public class TripDao {


    private static final Logger log = LoggerFactory.getLogger(TripDao.class);

    private static final PojoChecker checker = PojoChecker.of(TripResponseItem.class);

    private static final String TRIP_ID_FIELD = "id";
    private static final String TRIP_DATE_TIME_FIELD = "departureDttm";
    private static final String TRIP_HARBOUR_FIELD = "harbour";
    private static final String TRIP_TYPE_FIELD = "type";

    @Inject
    private MongoClient mongoClient;

    private MongoCollection<TripResponseItem> collection;

    @PostConstruct
    private void initialise() {
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        collection = mongoClient.getDatabase("junk").withCodecRegistry(pojoCodecRegistry).getCollection("trips", TripResponseItem.class);

        checker.check(TRIP_DATE_TIME_FIELD);
        checker.check(TRIP_HARBOUR_FIELD);
        checker.check(TRIP_TYPE_FIELD);
        checker.check(TRIP_ID_FIELD);
    }

    // TODO FIX THESE 2 METHODS


    public String addTrip(TripCreateRequest requestBody) {

        TripResponseItem item = new TripResponseItem();
        item.setHarbour(Harbour.PORT_SHELTER);
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

    private List<Bson> getFilterList(LocalDate date, Harbour harbour, TripType type) {

        List<Bson> filterList = new ArrayList<>();

        filterList.add(date != null ? onDay(TRIP_DATE_TIME_FIELD, date) : null);
        filterList.add(harbour != null ? eq(TRIP_HARBOUR_FIELD, harbour.name()) : null);
        filterList.add(type != null ? eq(TRIP_TYPE_FIELD, type.name()) : null);

        filterList.removeIf(Objects::isNull);

        return filterList;
    }

    public Set<TripResponseItem> getTrips(Integer offset, Integer limit,
                                          LocalDate date, Harbour harbour, TripType type) {

        List<Bson> filterList = getFilterList(date, harbour, type);

        FindIterable<TripResponseItem> foundItems;

        if (!filterList.isEmpty()) {
            Bson allFilters = and(filterList);
            foundItems = collection.find(allFilters);
        } else {
            foundItems = collection.find();
        }

        Set<TripResponseItem> ret = new LinkedHashSet<>();
        foundItems
                .sort(ascending(TRIP_ID_FIELD))
                .skip(offset)
                .limit(limit)
                .forEach((Consumer<? super TripResponseItem>) ret::add);
        return ret;

    }

    public Long getTotalCount(LocalDate date, Harbour harbour, TripType type) {

        List<Bson> filterList = getFilterList(date, harbour, type);

        if (!filterList.isEmpty()) {
            Bson allFilters = and(filterList);
            return collection.countDocuments(allFilters);
        } else {
            return collection.countDocuments();
        }

    }


    // helper methods

    private Bson inRange(String fieldName, LocalDateTime startDate, LocalDateTime endDate) {
        return and(gte(fieldName, startDate), lt(fieldName, endDate));

    }

    private Bson onDay(String fieldName, LocalDate day) {
        return inRange(fieldName, day.atStartOfDay(), day.plusDays(1L).atStartOfDay());
    }

    //    public Flowable<TripResponseItem> getTrips(Integer offset, Integer limit,
//                               LocalDate date, Harbour harbour, TripType type) {
//
//        List<Bson> filterList = getFilterList(date, harbour, type);
//
//        Flowable<TripResponseItem> foundItems;
//
//        if (!filterList.isEmpty()) {
//            Bson allFilters = and(filterList);
//            foundItems = Flowable.fromPublisher(collection.find(allFilters));
//
//        } else {
//            foundItems = Flowable.fromPublisher(collection.find());
//        }
//
//        return foundItems.skip(offset).limit(limit);
//
//    }

    //    public Flowable<Long> getTotalCount(LocalDate date, Harbour harbour, TripType type) {
//
//        List<Bson> filterList = getFilterList(date, harbour, type);
//
//        if (!filterList.isEmpty()) {
//            Bson allFilters = and(filterList);
//            return Flowable.fromPublisher(collection.countDocuments(allFilters));
//        } else {
//            return Flowable.fromPublisher(collection.countDocuments());
//        }
//
//    }

}
