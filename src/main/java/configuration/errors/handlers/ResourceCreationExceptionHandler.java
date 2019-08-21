package configuration.errors.handlers;

import configuration.errors.ErrorResponse;
import configuration.errors.exceptions.ResourceCreationException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Singleton
@Requires(classes = {ResourceCreationException.class, ExceptionHandler.class})
public class ResourceCreationExceptionHandler implements ExceptionHandler<ResourceCreationException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, ResourceCreationException exception) {
        return HttpResponse.badRequest(new ErrorResponse(exception.getMessageEnum().getMessage()));
    }
}
