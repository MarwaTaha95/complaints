package com.abc.complaints.entity;

/**
 * ADMIN_AUTHENTICATED: User is authenticated and of type admin,
 * <p>
 * USER_AUTHENTICATED: User is authenticated and of type user,
 * <p>
 * ANONYMOUS: User is not authenticated.
 */
public enum SessionState {
    ADMIN_AUTHENTICATED, USER_AUTHENTICATED, ANONYMOUS
}
