package com.abc.complaints.entity;

public enum Priority {
    URGENT("urgent"),
    CRITICAL("critical"),
    NORMAL("normal");

    private String code;

    Priority(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Priority getByCode(String code) {
        for (Priority e : Priority.values()) {
            if (e.getCode().equalsIgnoreCase(code)) {
                return e;
            }
        }
        return null;
    }
}
