package com.backend.blogApp.exception;

public class ResourceNotFoundException extends RuntimeException{

    String ResourceName;
    String fieldName;
    long fieldValue;
    String stringFieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s: %d", resourceName, fieldName, fieldValue));
        ResourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String stringFieldValue) {
        super(String.format("%s not found with %s: %d", resourceName, fieldName, stringFieldValue));
        ResourceName = resourceName;
        this.fieldName = fieldName;
        this.stringFieldValue = stringFieldValue;
    }
}
