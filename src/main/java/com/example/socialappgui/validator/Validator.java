package com.example.socialappgui.validator;

/**
 * validator interface that will be used for different types of entities
 * @param <T> - entity type
 */
public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
