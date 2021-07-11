package com.abc.complaints.validator.impl;

import com.abc.complaints.validator.EmailValidator;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

@Component
public class DefaultEmailValidator implements EmailValidator {
    private boolean isEmail(String email) {
        return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
    }

    @Override
    public boolean isValid(String email) {
        return !Strings.isNullOrEmpty(email) && isEmail(email);
    }
}
