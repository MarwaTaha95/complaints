package com.abc.complaints.repository.mock;

import com.abc.complaints.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractMockRepository<T> implements Repository<T, String> {
    protected ArrayList<T> entities = new ArrayList<>();

    @Override
    public Optional<T> findById(String s) {
        return entities.stream().filter(d -> Objects.equals(getId(d), s)).findFirst();
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entities);
    }

    @Override
    public T save(T entity) {
        if (!entities.contains(entity)) {
            entities.add(entity);
        }
        return entity;
    }

    @Override
    public void delete(T entity) {
        entities.removeIf(e -> Objects.equals(getId(e), getId(entity)));
    }

    @Override
    public abstract Class<T> getType();

    protected abstract String getId(T entity);
}
