package com.abc.complaints.entity;

public enum ComplaintStatus {
    PENDING("pending"), RESOLVED("resolved"), DISMISSED("dismissed");

    private String code;

    ComplaintStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ComplaintStatus getByCode(String code) {
        for (ComplaintStatus e : ComplaintStatus.values()) {
            if (e.getCode().equalsIgnoreCase(code)) {
                return e;
            }
        }
        return null;
    }
}
