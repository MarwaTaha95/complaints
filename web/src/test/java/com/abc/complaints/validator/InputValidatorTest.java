package com.abc.complaints.validator;

import com.abc.complaints.validator.impl.DefaultInputValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InputValidatorTest {
    private final InputValidator inputValidator = new DefaultInputValidator();

    @Test
    void testInputWithPrecedingForbiddenChar() {
        String input1 = "+test";
        String input2 = "@test";
        String input3 = "=test";

        String validatedInput1 = inputValidator.validateInput(input1);
        String validatedInput2 = inputValidator.validateInput(input2);
        String validatedInput3 = inputValidator.validateInput(input3);

        Assertions.assertTrue(validatedInput1.startsWith("'"));
        Assertions.assertTrue(validatedInput2.startsWith("'"));
        Assertions.assertTrue(validatedInput3.startsWith("'"));
    }

    @Test
    void testInputWithoutPrecedingForbiddenChar() {
        String input = "test";
        String validatedInput = inputValidator.validateInput(input);

        Assertions.assertFalse(validatedInput.startsWith("'"));
    }
}
