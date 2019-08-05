package configuration.service.daos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import configuration.service.errors.exceptions.ResourceAlreadyExistsException;
import configuration.service.errors.exceptions.ResourceNotFoundException;
import configuration.service.request.CompanyCreateRequest;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.mongodb.client.model.Filters.eq;

public class CompanyDao {


    private static final Logger log = LoggerFactory.getLogger(CompanyDao.class);

    @Inject
    ObjectMapper objectMapper;

    @Inject
    private MongoClient mongoClient;

    private MongoCollection<Document> collection;

    @PostConstruct
    private void initialise() {
        collection = mongoClient.getDatabase("test").getCollection("companies");
    }


    public String addCompany(CompanyCreateRequest requestBody) {

        try {
            log.info("attempting to add company: " + objectMapper.writeValueAsString(requestBody));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // TODO change this to actually error when a user has more than one company attached to them
        if (collection.find(eq("code", requestBody.getCode())).first() != null) {
            log.info("failed - entry already exists for company code: " + requestBody.getCode());
            throw new ResourceAlreadyExistsException(requestBody.getCode());
        }
        Document doc = objectMapper.convertValue(requestBody, Document.class);
        collection.insertOne(doc);
        log.info("company added successfully, assigned DB id: " + doc.get("_id").toString());
        return doc.get("_id").toString();
    }

    public CompanyCreateRequest getCompany(String companyId) {

        Document doc = collection.find(eq("_id", new ObjectId(companyId))).first();
        if (doc != null) {
            return objectMapper.convertValue(doc, CompanyCreateRequest.class);
        } else {
            throw new ResourceNotFoundException();
        }

    }

}
