package com.abc.complaints.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    Optional<T> findById(ID id);

    List<T> findAll();

    T save(T entity);

    Class<T> getType();

    void delete(T entity);
}