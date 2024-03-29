package com.abc.complaints.service.impl;

import com.abc.complaints.Constants;
import com.abc.complaints.entity.Person;
import com.abc.complaints.entity.RoleType;
import com.abc.complaints.entity.Session;
import com.abc.complaints.entity.SessionState;
import com.abc.complaints.repository.PersonRepository;
import com.abc.complaints.service.SessionService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;

/**
 * Session service
 */
@Service
public class DefaultSessionService implements SessionService {

    private final PersonRepository people;

    public DefaultSessionService(PersonRepository people) {
        this.people = people;
    }

    /**
     * Create a new session from an HttpSession
     */
    public Session createSession(HttpSession httpSession) {
        if (httpSession == null) {
            return null;
        }

        Session session = new Session();
        session.setGlobalId(httpSession.getId());

        httpSession.setAttribute(Constants.Attributes.SESSION, session);
        return session;
    }

    /**
     * Get session from HttpSession if it exists
     */
    public Session getExistingSession(HttpSession httpSession) {
        if (httpSession == null) {
            return null;
        }

        return (Session) httpSession.getAttribute(Constants.Attributes.SESSION);
    }

    /**
     * Get a session from HttpSession, if exists, else, create a new one
     * */
    public Session getSession(HttpSession httpSession) {
        Session session = getExistingSession(httpSession);
        if (session == null) {
            session = createSession(httpSession);
        }

        return session;
    }

    /**
     * Get the person authenticated in session
     * */
    @Override
    public Person getPerson(Session session) {
        Optional<Person> person = session.getIdentityIds().stream().flatMap(identity -> people.findById(identity).stream()).findFirst();
        return person.orElse(null);
    }

    /**
     * Add person to the session
     * */
    @Override
    public void authenticate(Session session, Person person) {
        session.getIdentityIds().add(person.getId());
    }

    /**
     * Get the state of the session
     * */
    @Override
    public SessionState getState(Session session) {
        Person person = getPerson(session);

        if (person != null) {
            return person.getRoleType().equals(RoleType.ADMIN) ? SessionState.ADMIN_AUTHENTICATED : SessionState.USER_AUTHENTICATED;
        } else {
            return SessionState.ANONYMOUS;
        }
    }

    /**
     * remove user form session
     * */
    @Override
    public void logout(Session session) {
        session.setIdentityIds(new HashSet<>());
    }
}