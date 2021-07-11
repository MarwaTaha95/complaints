package com.abc.complaints.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SendgridConfig {
    @Value("${todos.sendgrid.apiKey}")
    private String apiKey;

    @Value("${todos.sendgrid.sender}")
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