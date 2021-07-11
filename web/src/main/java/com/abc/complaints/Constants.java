package com.abc.complaints;

public interface Constants {

    interface Attributes {
        String SESSION = "session";
        String REMEMBER_ME = "REMEMBER_ME";
        String STATE = "State";
    }

    interface EMAIL_TEMPLATES {
        String ACCOUNT_VERIFICATION = "d-4de03ea04b5141669bf33a581b1e2be0";
        String WELCOME = "not created yet";
    }

    interface Registration {
        String PENDING_CREATION = "pending_creation";
        String TOTP = "totp";
    }

    interface IDENTITIES {
        String PERSON = "person";
    }
}

