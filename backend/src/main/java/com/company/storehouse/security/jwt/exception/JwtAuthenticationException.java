package com.company.storehouse.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception would be thrown if a token was invalid or expired
 */
public class JwtAuthenticationException extends AuthenticationException {
    private static final long serialVersionUID = -4318197935208346307L;

    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
