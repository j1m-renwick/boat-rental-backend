package configuration.errors.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {

    String resourceId;

    public ResourceAlreadyExistsException(String resourceId) {
        super();
        this.resourceId = resourceId;
    }

    public String getResourceId() {
        return resourceId;
    }
}
