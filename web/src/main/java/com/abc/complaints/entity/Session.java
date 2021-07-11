package com.abc.complaints.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Session {
    String id = UUID.randomUUID().toString();
    String globalId;
    Map<String, Object> attributes = new HashMap<>();
    List<String> identityIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<String> getIdentityIds() {
        return identityIds;
    }

    public void setIdentityIds(List<String> identityIds) {
        this.identityIds = identityIds;
    }

    public <T> T getAttribute(String name) {
        return (T) this.attributes.get(name);
    }

    public void putAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    public <T> T removeAttribute(String name) {
        return (T) this.attributes.remove(name);
    }
}
