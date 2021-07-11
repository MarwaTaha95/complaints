package com.abc.complaints.repository.mock;

import com.abc.complaints.entity.Person;
import com.abc.complaints.repository.PersonRepository;

public class MockPersonRepository extends AbstractMockRepository<Person> implements PersonRepository {
    @Override
    public Class<Person> getType() {
        return Person.class;
    }

    @Override
    protected String getId(Person entity) {
        return entity.getId();
    }

    @Override
    public Person findByEmail(String email) {
        return entities.stream().filter(entity -> entity.getEmail().equals(email)).findFirst().orElse(null);
    }
}
