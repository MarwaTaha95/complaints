package com.abc.complaints.controller;

import com.abc.complaints.entity.Session;
import com.abc.complaints.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/rest/session")
public class SessionController  {

    private final SessionService sessions;
    private final Session session;


    public SessionController(SessionService sessions, Session session) {
        this.sessions = sessions;
        this.session = session;
    }

    @GetMapping(value = "/state")
    public Object state(HttpServletRequest httpServletRequest) {
        return sessions.getState(session);
    }

    @GetMapping(value = "/logout")
    public Object logout(HttpServletRequest httpServletRequest) {
        sessions.logout(session);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
