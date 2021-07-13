package com.abc.complaints.entity;

/**
 * An object used to store the one time password in session in order to be validated
 * later.
 * <p>
 * The code is for actual generated code
 * The secret is to be used in creating and validating the code
 * The source is an identifier for the user (email address)
 */
public class Totp {
    private String source;
    private String secret;
    private int code;
    private long timestamp;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
