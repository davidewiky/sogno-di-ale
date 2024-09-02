package it.ale.exception;

public class GenericException extends Exception {

    public GenericException() {
        super();
    }

    public GenericException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public GenericException(final String message) {
        super(message);
    }

    public GenericException(final Throwable throwable) {
        super(throwable);
    }
}
