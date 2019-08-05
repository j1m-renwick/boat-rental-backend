package configuration.service.daos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import configuration.service.errors.exceptions.ResourceAlreadyExistsException;
import configuration.service.errors.exceptions.ResourceNotFoundException;
import configuration.service.request.JunkCreateRequest;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.mongodb.client.model.Filters.eq;

public class JunkDao {


    private static final Logger log = LoggerFactory.getLogger(JunkDao.class);

    @Inject
    ObjectMapper objectMapper;

    @Inject
    private MongoClient mongoClient;

    private MongoCollection<Document> collection;

    @PostConstruct
    private void initialise() {
        collection = mongoClient.getDatabase("test").getCollection("junks");
    }


    public String addJunk(JunkCreateRequest requestBody) {

        try {
            log.info("attempting to add junk: " + objectMapper.writeValueAsString(requestBody));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // TODO check that:
        // a) company referenced in post request is tied to the user requesting the addition
        // b) the boat name/code is not the same as another under the company
        // c)
        if (collection.find(eq("code", requestBody.getCode())).first() != null) {
            log.info("failed - entry already exists for junk code: " + requestBody.getCode());
            throw new ResourceAlreadyExistsException(requestBody.getCode());
        }
        Document doc = objectMapper.convertValue(requestBody, Document.class);
        collection.insertOne(doc);
        log.info("junk added successfully, assigned DB id: " + doc.get("_id").toString());
        return doc.get("_id").toString();
    }

    public JunkCreateRequest getJunk(String junkId) {

        // TODO reject if the record found does not belong to the user requesting it
        Document doc = collection.find(eq("_id", new ObjectId(junkId))).first();
        if (doc != null) {
            return objectMapper.convertValue(doc, JunkCreateRequest.class);
        } else {
            throw new ResourceNotFoundException();
        }

    }

}
