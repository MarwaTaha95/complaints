package com.abc.complaints.service;

import com.abc.complaints.entity.Totp;

public interface TotpService {
    Totp generate(String source);

    boolean validateCode(Totp totp, String code);
}