package com.emce.creditsservice.entity;

import lombok.Getter;

public enum Status {
    WAITING_APPROVAL(1),
    APPROVED(2),
    DEPTOR(3),
    PARTIALLY_AMORTIZED(4),
    AMORTIZED(5)
    ;
    @Getter
    int status;

    Status(int status) {
        this.status = status;
    }
    public static Status getStatus(String statusInput) {
        try {
            return Status.valueOf(statusInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            // If it fails, continue to check if the input is an ordinal (int) value
            try {
                int statusOrdinal = Integer.parseInt(statusInput);
                for (Status status : Status.values()) {
                    if (status.getStatus() == statusOrdinal) {
                        return status;
                    }
                }
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        return null;
    }
}
