package ru.magenta.exception;

public class EmptyCityPassed extends RuntimeException {
    public EmptyCityPassed(String message) {
        super(message);
    }
}
