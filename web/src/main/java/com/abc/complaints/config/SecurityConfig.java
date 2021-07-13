package com.abc.complaints.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This config file specifies BCryptPasswordEncoder to be used for
 * encoding passwords.
 */
@Configuration
public class SecurityConfig {

    /**
     * @return a passwordEncoder bean of type BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}