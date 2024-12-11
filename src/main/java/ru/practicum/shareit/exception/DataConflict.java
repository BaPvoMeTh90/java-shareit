package ru.practicum.shareit.exception;

public class DataConflict extends RuntimeException {
    public DataConflict(String message) {
        super(message);
    }
}
