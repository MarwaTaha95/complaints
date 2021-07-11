package com.abc.complaints.service;

import com.abc.complaints.Constants;
import com.abc.complaints.entity.Session;
import com.abc.complaints.repository.PersonRepository;
import com.abc.complaints.repository.mock.MockPersonRepository;
import com.abc.complaints.service.impl.DefaultSessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionServiceTest {
    private final SessionService sessionService = new DefaultSessionService(null);

    @Test
    public void testCreateNewSessionObject() {
        HttpSession httpSession = mock(HttpSession.class);
        Session session = sessionService.createSession(httpSession);

        Assertions.assertEquals(session.getGlobalId(), httpSession.getId());
    }

    @Test
    public void testAlreadyExistingSession() {
        HttpSession httpSession = mock(HttpSession.class);
        Session session = new Session();
        session.setGlobalId(httpSession.getId());

        when(httpSession.getAttribute(Constants.Attributes.SESSION)).thenReturn(session);

        Session existingSession = sessionService.getExistingSession(httpSession);
        Assertions.assertEquals(existingSession.getGlobalId(), httpSession.getId());
    }

    @Test
    public void testNullHttpSession() {
        HttpSession httpSession = null;

        Session newSession = sessionService.createSession(httpSession);
        Assertions.assertNull(newSession);

        Session existingSession = sessionService.getExistingSession(httpSession);
        Assertions.assertNull(existingSession);
    }

    @Test
    public void testGetSessionForNewSessions(){
        HttpSession httpSession = mock(HttpSession.class);
        Session session = sessionService.getSession(httpSession);

        Assertions.assertEquals(session.getGlobalId(), httpSession.getId());
    }

    @Test
    public void testGetSessionForAlreadyExistingSessions(){
        HttpSession httpSession = mock(HttpSession.class);
        Session session = new Session();
        session.setGlobalId(httpSession.getId());

        when(httpSession.getAttribute(Constants.Attributes.SESSION)).thenReturn(session);

        Session existingSession = sessionService.getSession(httpSession);

        Assertions.assertEquals(existingSession.getGlobalId(), httpSession.getId());
        Assertions.assertEquals(existingSession.getId(), session.getId());
    }
}