package com.abc.complaints.repository.impl;


import com.abc.complaints.entity.Person;
import com.abc.complaints.repository.PersonRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MongoPersonRepository extends AbstractMongoRepository<Person> implements PersonRepository {
    public MongoPersonRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate, Person.class);
    }

    @Override
    public Class<Person> getType() {
        return Person.class;
    }

    @Override
    public Person findByEmail(String email) {
        return mongoTemplate.findOne(Query.query(Criteria.where("email").is(email)), Person.class);
    }
}
