package com.abc.complaints.config;

import com.abc.complaints.entity.Session;
import com.abc.complaints.service.SessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class SessionConfig {
    private final SessionService sessions;

    public SessionConfig(SessionService sessions) {
        this.sessions = sessions;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session session(HttpServletRequest request) {
        var session = sessions.getSession(request.getSession());

        if (session == null) {
            throw new IllegalStateException("A session should have been created by now!");
        }

        return session;
    }
}