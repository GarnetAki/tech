package ru.soloviev.Dao;

import java.util.List;

public interface Dao<T> {
    T find(Integer id);

    T save(T t);

    T update(T t);

    T delete(T t);

    List<T> findAll();
}
