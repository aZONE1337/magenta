package ru.magenta.exception;

import org.hibernate.HibernateException;

public class NoSuchDistanceMatrixRecord extends HibernateException {
    public NoSuchDistanceMatrixRecord(String message) {
        super(message);
    }
}
