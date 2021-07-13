package com.abc.complaints.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This config file represents a wrapper for all of sendGrids configurations.
 * The actual values are to be supplied as properties in application.properties.
 */
@Component
public class SendgridConfig {
    @Value("${sendgrid.apiKey}")
    private String apiKey;

    @Value("${sendgrid.sender}")
    private String sender;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}