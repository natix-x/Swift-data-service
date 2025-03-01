package com.bankdata.swiftmanager.exception;

public class CountryNotFoundException extends ResourceNotFoundException {
    public CountryNotFoundException(String message) {
        super(message);
    }
}
