package com.example.socialtpygui.service.validators;


public interface Validator<T> {
    void validate(T entity) throws ValidationException;

}