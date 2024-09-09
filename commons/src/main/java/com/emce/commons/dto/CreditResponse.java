package com.emce.commons.dto;

import com.emce.commons.entity.Credit;
import lombok.Builder;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record CreditResponse(Integer userId, Integer creditId, String status, Double amount, Set<InstallmentDto> installments) {
    public static CreditResponse fromEntity(Credit credit) {
        return CreditResponse.builder()
                .creditId(credit.getId())
                .userId(credit.getUserId())
                .status(credit.getStatus().name())
                .amount(credit.getAmount())
                .installments(credit.getInstallments().stream().map(InstallmentDto::fromEntity).collect(Collectors.toSet()))
                .build();
    }
}
