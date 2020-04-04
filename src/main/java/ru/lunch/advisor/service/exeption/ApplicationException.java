package ru.lunch.advisor.service.exeption;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String msg) {
        super(msg);
    }
}
