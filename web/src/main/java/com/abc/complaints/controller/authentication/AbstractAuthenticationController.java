package com.abc.complaints.controller.authentication;

import com.abc.complaints.entity.Person;
import com.abc.complaints.entity.Session;
import com.abc.complaints.exception.entity.AlreadyAuthenticatedException;
import com.abc.complaints.exception.entity.EmptyFieldException;
import com.abc.complaints.service.SessionService;
import com.google.common.base.Strings;

/**
 * This class represents a high level class for authenticating users.
 * Contains the common operations of all Auth types.
 */
public class AbstractAuthenticationController {
    private final Session session;
    private final SessionService sessions;

    public AbstractAuthenticationController(Session session, SessionService sessions) {
        this.session = session;
        this.sessions = sessions;
    }

    /**
     * Authenticate user into session
     *
     * @param person to be authenticated
     */
    protected void authenticateUser(Person person) {
        sessions.authenticate(session, person);
    }

    /**
     * Validate that there is no user already authenticated in session,
     * before starting an auth flow.
     *
     * @throws AlreadyAuthenticatedException if there is already a user in session
     */
    protected void validateUserNotAuthenticated() throws AlreadyAuthenticatedException {
        Person existingPerson = sessions.getPerson(session);
        if (existingPerson != null) {
            throw new AlreadyAuthenticatedException("User already logged in");
        }
    }

    /**
     * Validate param is not null.
     *
     * @throws EmptyFieldException if the param is null.
     */
    protected void validateParamNotNull(String param, String name) throws EmptyFieldException {
        if (Strings.isNullOrEmpty(param)) {
            throw new EmptyFieldException(name + " cannot be null");
        }
    }
}
