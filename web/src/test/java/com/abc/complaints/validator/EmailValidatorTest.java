package com.abc.complaints.validator;

import com.abc.complaints.validator.impl.DefaultEmailValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmailValidatorTest {

    EmailValidator emailValidator = new DefaultEmailValidator();

    @Test
    void testWrongEmail() {
        String wrongEmail1 = "wrong.@gmail";
        String wrongEmail2 = "wrong..@ert";

        boolean valid1 = emailValidator.isValid(wrongEmail1);
        boolean valid2 = emailValidator.isValid(wrongEmail2);

        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
    }

    @Test
    void testEmptyEmail() {
        String email = "";
        boolean isValid = emailValidator.isValid(email);

        Assertions.assertFalse(isValid);
    }

    @Test
    void testCorrectEmail() {
        String email = "test@gmail.com";
        String email2 = "test@me.co.uk";

        boolean isValid = emailValidator.isValid(email);
        boolean isValid2 = emailValidator.isValid(email2);

        Assertions.assertTrue(isValid);
        Assertions.assertTrue(isValid2);
    }
}
