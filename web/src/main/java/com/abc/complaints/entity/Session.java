package com.abc.complaints.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An entity for a user's session.
 * <p>
 * It can be used to store user, and other attributes related to users.
 * The globalId is to be used as a cookie value
 */
public class Session implements Serializable {
    String id = UUID.randomUUID().toString();
    String globalId;
    Map<String, Object> attributes = new ConcurrentHashMap<>();
    Set<String> identityIds = new HashSet<>();

    public Session() {
    }

    public Set<String> getIdentityIds() {
        return identityIds;
    }

    public void setIdentityIds(Set<String> identityIds) {
        this.identityIds = identityIds;
    }

    public String getId() {
        return id;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
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

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
