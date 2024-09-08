package com.emce.paymentservice.entity;

import lombok.Getter;

public enum InstallmentStatus {
    DEPTOR(1),
    PARTIALLY_AMORTIZED(2),
    AMORTIZED(3)
    ;
    @Getter
    int status;

    InstallmentStatus(int status) {
        this.status = status;
    }
}
