package com.abc.complaints.controller.authentication;

import com.abc.complaints.entity.Person;
import com.abc.complaints.entity.Session;
import com.abc.complaints.exception.entity.AlreadyAuthenticatedException;
import com.abc.complaints.exception.entity.EmptyFieldException;
import com.abc.complaints.service.SessionService;
import com.google.common.base.Strings;

public class AbstractAuthenticationController {
    private final Session session;
    private final SessionService sessions;

    public AbstractAuthenticationController(Session session, SessionService sessions) {
        this.session = session;
        this.sessions = sessions;
    }

    protected void authenticateUser(Person person) {
        sessions.authenticate(session, person);
    }

    protected void validateUserNotAuthenticated() throws AlreadyAuthenticatedException {
        Person existingPerson = sessions.getPerson(session);
        if (existingPerson != null) {
            throw new AlreadyAuthenticatedException("User already logged in"); //return to dashboard
        }
    }

    protected void validateParamNotNull(String param, String name) throws EmptyFieldException {
        if (Strings.isNullOrEmpty(param)) {
            throw new EmptyFieldException(name + " cannot be null");
        }
    }
}
