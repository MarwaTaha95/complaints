package com.abc.complaints.service.impl;

import com.abc.complaints.entity.Totp;
import com.abc.complaints.service.TotpService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultTotpService implements TotpService {
    private GoogleAuthenticator googleAuthenticator;

    /**
     * Constructor
     * <p>
     * used to initialize google authenticator that will be used for creating and validating
     * a one-time password for users.
     */
    public DefaultTotpService(@Value("${totp.window.size}") int windowSize, @Value("${totp.millis}") int millis) {
        this.googleAuthenticator = new GoogleAuthenticator(
                new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                        .setTimeStepSizeInMillis(millis)
                        .setWindowSize(windowSize)
                        .build());
    }

    /**
     * Generate a one-time password for a user
     *
     * @param source, identifier of the user (email)
     * @return Totp object containing the generated code and the secret to validate it
     */
    public Totp generate(String source) {
        var totp = new Totp();

        totp.setSecret(createSecret());
        totp.setSource(source);
        totp.setCode(googleAuthenticator.getTotpPassword(totp.getSecret()));
        totp.setTimestamp(System.currentTimeMillis());

        return totp;
    }

    /**
     * Generate a random secret to be used for creating totp
     */
    private String createSecret() {
        String secret = UUID.randomUUID().toString().replace("-", "").substring(1, 11);
        secret = new Base32().encodeAsString(secret.getBytes());
        return secret;
    }

    /**
     * Validate the received code using the secret that was used for creating it
     */
    public boolean validateCode(Totp totp, String code) {
        try {
            int value = Integer.parseInt(code);
            return googleAuthenticator.authorize(totp.getSecret(), value);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}