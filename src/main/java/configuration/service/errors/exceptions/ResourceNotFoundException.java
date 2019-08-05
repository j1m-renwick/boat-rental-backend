package configuration.service.errors.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("resource was not located");
    }

}
