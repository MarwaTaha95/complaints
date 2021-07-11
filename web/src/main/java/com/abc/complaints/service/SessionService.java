package com.abc.complaints.service;

import com.abc.complaints.entity.Person;
import com.abc.complaints.entity.Session;

import javax.servlet.http.HttpSession;

public interface SessionService {
    Session createSession(HttpSession httpSession);

    Session getExistingSession(HttpSession httpSession);

    Session getSession(HttpSession httpSession);

    Person getPerson(Session session);

    void authenticate(Session session, Person person);
}