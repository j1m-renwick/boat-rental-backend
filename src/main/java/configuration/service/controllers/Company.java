package configuration.service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import configuration.service.daos.CompanyDao;
import configuration.service.request.CompanyCreateRequest;
import configuration.service.response.CreatedResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;

@Controller("/company")
public class Company {

    @Inject
    private CompanyDao companyDao;

    //    @Override
//    public Single add(Product product) {
//        return Single.fromPublisher(
//                getCollection().insertOne(product)
//        ).map(success -> product);
//    }
//    @Override
//    public Single<List> findAll() {
//        return Flowable.fromPublisher(
//                getCollection().find()
//        ).toList();
//    }
//    @Override
//    public Maybe findOne(String productCode) {
//        return Flowable.fromPublisher(
//                getCollection()
//                        .find(Filters.eq("code", productCode))
//                        .limit(1)
//        ).firstElement();
//    }
//    private MongoCollection getCollection() {
//        return mongoClient
//                .getDatabase("products-demo")
//                .getCollection("product", Product.class);
//    }

    // TODO add all CRUD

    @Post()
    public CreatedResponse addCompany(CompanyCreateRequest requestBody) {
        return new CreatedResponse(companyDao.addCompany(requestBody));

    }

    @Get("/{companyId}")
    public CompanyCreateRequest getCompany(String companyId) {
        return companyDao.getCompany(companyId);
    }
}