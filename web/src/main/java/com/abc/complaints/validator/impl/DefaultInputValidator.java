package com.abc.complaints.validator.impl;

import com.abc.complaints.validator.InputValidator;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validate input against injection attacks
 * */
@Component
public class DefaultInputValidator implements InputValidator {

    private static final Pattern PATTERN = Pattern.compile("^([=+\\-@]).*$");
    private static final String PREFIX = "'";

    private boolean isValid(String input) {
        Matcher matcher = PATTERN.matcher(input);
        return !matcher.find();
    }

    @Override
    public String validateInput(String input) {
        if (Strings.isNullOrEmpty(input) || isValid(input)) {
            return input.trim();
        } else {
            return PREFIX + input.trim();
        }
    }
}

