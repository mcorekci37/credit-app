package com.emce.paymentservice.dto;

public record PaymentRequest(Integer installmentId, Double amount) {
}
