package configuration.errors.handlers;

import configuration.errors.exceptions.ResourceAlreadyExistsException;
import configuration.services.LinkService;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Requires(classes = {ResourceAlreadyExistsException.class, ExceptionHandler.class})
public class ResourceAlreadyExistsExceptionHandler implements ExceptionHandler<ResourceAlreadyExistsException, HttpResponse> {

    @Inject
    LinkService linkService;

    @Override
    public HttpResponse handle(HttpRequest request, ResourceAlreadyExistsException exception) {
        return HttpResponse.seeOther(linkService.getSelfUri(request, exception.getResourceId()));
    }
}
