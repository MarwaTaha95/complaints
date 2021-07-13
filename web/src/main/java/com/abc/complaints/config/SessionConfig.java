package com.abc.complaints.config;

import com.abc.complaints.entity.Session;
import com.abc.complaints.service.SessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.servlet.http.HttpServletRequest;

/**
 * This config file creates a bean of type Session, to store
 * user's data, and make it accessible for all services.
 */
@Configuration
public class SessionConfig {
    private final SessionService sessions;

    public SessionConfig(SessionService sessions) {
        this.sessions = sessions;
    }

    /**
     * Create a session bean and scope it per request, to be aligned with
     * the HttpSession.
     *
     * @return a session bean.
     */
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