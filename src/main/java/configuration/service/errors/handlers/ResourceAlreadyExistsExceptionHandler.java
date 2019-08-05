package configuration.service.errors.handlers;

import configuration.service.errors.ErrorResponse;
import configuration.service.errors.exceptions.ResourceAlreadyExistsException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;

import javax.inject.Singleton;

@Singleton
@Requires(classes = {ResourceAlreadyExistsException.class, ExceptionHandler.class})
public class ResourceAlreadyExistsExceptionHandler implements ExceptionHandler<ResourceAlreadyExistsException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, ResourceAlreadyExistsException exception) {

        return HttpResponse.badRequest(new ErrorResponse(exception.getMessage()));
    }
}
