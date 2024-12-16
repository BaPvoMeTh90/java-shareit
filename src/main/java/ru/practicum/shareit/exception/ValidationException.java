package ru.practicum.shareit.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}