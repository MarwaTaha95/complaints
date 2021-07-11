package com.abc.complaints.repository;

import com.abc.complaints.entity.Person;

public interface PersonRepository extends Repository<Person, String> {
    Person findByEmail(String email);
}
