package configuration.errors.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String id) {
        super(String.format("a resource already exists that is identified by %s", id));
    }

    public ResourceAlreadyExistsException() {
        super("a resource with the supplied parameters already exists");
    }
}
