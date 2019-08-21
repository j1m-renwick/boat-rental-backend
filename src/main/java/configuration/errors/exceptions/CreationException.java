package configuration.errors.exceptions;

public enum CreationException {

    QUANTITY_TOO_LARGE("the request quantity is greater than available"),
    BODY_PARAMS_NULL("some mandatory fields were not supplied in the request body");

    String message;

    CreationException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
