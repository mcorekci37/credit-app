package com.emce.creditsservice.dto;

public record CreditRequest(Integer userId, Double amount, Integer installmentCount) {
}
