package configuration.errors.handlers;

import configuration.errors.ErrorResponse;
import configuration.errors.exceptions.ResourceAlreadyExistsException;
import configuration.errors.exceptions.ResourceNotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Singleton
@Requires(classes = {ResourceNotFoundException.class, ExceptionHandler.class})
public class ResourceNotFoundExceptionHandler implements ExceptionHandler<ResourceNotFoundException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, ResourceNotFoundException exception) {

        return HttpResponse.notFound(new ErrorResponse(exception.getMessage()));
    }
}
