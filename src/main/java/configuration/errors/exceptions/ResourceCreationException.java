package configuration.errors.exceptions;

public class ResourceCreationException extends RuntimeException {

    CreationException messageEnum;

    public ResourceCreationException() {
        super();
    }

    public ResourceCreationException(CreationException exceptionEnum) {
        super();
        messageEnum = exceptionEnum;
    }

    public CreationException getMessageEnum() {
        return messageEnum;
    }
}
