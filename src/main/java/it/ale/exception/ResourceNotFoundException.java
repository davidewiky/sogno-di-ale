package it.ale.exception;

public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(String object, String field, Long fieldvalue) {
        super(object + " with " + field + " : " + fieldvalue + " NOT EXISTS");
    }

    public ResourceNotFoundException(String object, String field, String fieldvalue) {
        super(object + " with " + field + " : " + fieldvalue + " NOT EXISTS");
    }

    private static final long serialVersionUID = -6018113063371956892L;
}
