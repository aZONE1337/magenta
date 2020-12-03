package ru.magenta.exception;

public class EmptyDistanceListPassed extends RuntimeException {
    public EmptyDistanceListPassed(String message) {
        super(message);
    }
}
