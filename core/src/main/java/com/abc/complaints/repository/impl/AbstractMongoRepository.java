package com.abc.complaints.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import com.abc.complaints.repository.Repository;

import java.util.List;
import java.util.Optional;

public class AbstractMongoRepository<T> implements Repository<T, String> {
    protected final MongoTemplate mongoTemplate;
    protected final Class<T> type;

    public AbstractMongoRepository(MongoTemplate mongoTemplate, Class<T> type) {
        this.mongoTemplate = mongoTemplate;
        this.type = type;
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, type));
    }

    @Override
    public List<T> findAll() {
        return mongoTemplate.find(new Query().restrict(type), type);
    }

    @Override
    public T save(T entity) {
        mongoTemplate.save(entity);
        return entity;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public void delete(T entity) {
        mongoTemplate.remove(entity);
    }
}