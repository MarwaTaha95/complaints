package com.abc.complaints.service;

import com.abc.complaints.entity.Totp;
import com.abc.complaints.service.impl.DefaultTotpService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TotpServiceTest {
    private TotpService totpService = new DefaultTotpService(4, 3000);

    @Test
    void testGenerateTotp() {
        Totp generate = totpService.generate("test1.com");

        Assertions.assertTrue(generate.getCode() > 0);
        Assertions.assertEquals(generate.getSource(), "test1.com");
        Assertions.assertTrue(generate.getTimestamp() < System.currentTimeMillis());

    }

    @Test
    void testValidateTotpSuccessfully() {
        Totp generate = totpService.generate("test2.com");
        boolean isValid = totpService.validateCode(generate, Integer.toString(generate.getCode()));
        Assertions.assertTrue(isValid);
    }

    @Test
    void testValidateTotpWithWrongCode() {
        Totp generate = totpService.generate("test3.com");
        boolean isValid = totpService.validateCode(generate, "123456");
        Assertions.assertFalse(isValid);
    }

    @Test
    void testValidateTotpWithExpiredCode() throws InterruptedException {
        Totp generate = totpService.generate("test4.com");
        Thread.sleep(7000);
        boolean isValid = totpService.validateCode(generate, Integer.toString(generate.getCode()));
        Assertions.assertFalse(isValid);
    }

    @Test
    void testValidateTotpCodeWithWrongFormat() {
        Totp generate = totpService.generate("test5.com");
        boolean isValid = totpService.validateCode(generate, "test");
        Assertions.assertFalse(isValid);
    }
}
