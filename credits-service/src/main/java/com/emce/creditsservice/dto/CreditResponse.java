package com.emce.creditsservice.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record CreditResponse(Integer userId, Integer creditId, Set<InstallmentDto> installments) {
}
