package configuration.service.controllers;

import configuration.service.daos.JunkDao;
import configuration.service.request.CompanyCreateRequest;
import configuration.service.request.JunkCreateRequest;
import configuration.service.response.CreatedResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;

@Controller("/junk")
public class Junk {

    @Inject
    private JunkDao junkDao;

    // TODO add all CRUD

    @Post()
    public CreatedResponse addJunk(JunkCreateRequest requestBody) {
        return new CreatedResponse(junkDao.addJunk(requestBody));

    }

    @Get("/{junkId}")
    public JunkCreateRequest getJunk(String junkId) {
        return junkDao.getJunk(junkId);
    }
}