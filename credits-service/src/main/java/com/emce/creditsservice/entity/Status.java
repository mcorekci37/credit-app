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
}
