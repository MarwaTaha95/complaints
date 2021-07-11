package com.abc.complaints.service.impl;

import com.abc.complaints.Constants;
import com.abc.complaints.entity.Person;
import com.abc.complaints.entity.Session;
import com.abc.complaints.repository.PersonRepository;
import com.abc.complaints.service.SessionService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class DefaultSessionService implements SessionService {

    private final PersonRepository people;

    public DefaultSessionService(PersonRepository people) {
        this.people = people;
    }

    public Session createSession(HttpSession httpSession) {
        if (httpSession == null) {
            return null;
        }

        Session session = new Session();
        session.setGlobalId(httpSession.getId());

        httpSession.setAttribute(Constants.Attributes.SESSION, session);
        return session;
    }

    public Session getExistingSession(HttpSession httpSession) {
        if (httpSession == null) {
            return null;
        }

        return (Session) httpSession.getAttribute(Constants.Attributes.SESSION);
    }

    public Session getSession(HttpSession httpSession) {
        Session session = getExistingSession(httpSession);
        if (session == null) {
            session = createSession(httpSession);
        }

        return session;
    }

    @Override
    public Person getPerson(Session session) {
        Optional<Person> person = session.getIdentityIds().stream().flatMap(identity -> people.findById(identity).stream()).findFirst();
        return person.orElse(null);
    }

    @Override
    public void authenticate(Session session, Person person) {
        session.getIdentityIds().add(person.getId());
    }
}