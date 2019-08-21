package configuration.errors.exceptions;

public enum CreationException {

    INVALID_QUANTITY("the request quantity is greater than available");

    String message;

    CreationException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
