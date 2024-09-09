package com.emce.commons.dto;

public record CreditRequest(Integer userId, Double amount, Integer installmentCount) {
}
