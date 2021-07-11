package com.abc.complaints.service;

import com.abc.complaints.entity.Session;

import javax.servlet.http.HttpSession;

public interface SessionService {
    Session createSession(HttpSession httpSession);

    Session getExistingSession(HttpSession httpSession);

    Session getSession(HttpSession httpSession);
}