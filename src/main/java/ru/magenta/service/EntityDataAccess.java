package ru.magenta.service;

import java.util.List;

public interface EntityDataAccess<K, E> {
    List<E> getAll();

    E get(K key);

    void save(E entity);

    void update(K keyOld, E newEntity);

    void delete(K key);
}
