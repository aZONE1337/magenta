package ru.magenta.exception;

public class DifferentListSizeArguments extends RuntimeException {
    public DifferentListSizeArguments(String message) {
        super(message);
    }
}
