package com.abc.complaints.entity;

public enum Client {
    CLIENT1("client1"),
    CLIENT2("client2"),
    CLIENT3("client3");

    private String code;

    Client(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Client getByCode(String code) {
        for (Client e : Client.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
