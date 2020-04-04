package ru.lunch.advisor.service.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "NOT_FOUND ";
    private final HashMap<String, Object> parameters = new HashMap<>(2);

    public NotFoundException() {
    }

    public NotFoundException(Long code) {
        super(NOT_FOUND + code);
    }

    public NotFoundException(String name) {
        super(NOT_FOUND + name);
    }
}
