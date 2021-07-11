package com.abc.complaints.service.impl;

import com.abc.complaints.Constants;
import com.abc.complaints.entity.Session;
import com.abc.complaints.service.SessionService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class DefaultSessionService implements SessionService {

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
}