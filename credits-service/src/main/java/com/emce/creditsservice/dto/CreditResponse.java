package com.emce.creditsservice.dto;

import com.emce.creditsservice.entity.Credit;
import lombok.Builder;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record CreditResponse(Integer userId, Integer creditId, String status, Set<InstallmentDto> installments) {
    public static CreditResponse fromEntity(Credit credit) {
        return CreditResponse.builder()
                .userId(credit.getUserId())
                .creditId(credit.getId())
                .status(credit.getStatus().name())
                .installments(credit.getInstallments().stream().map(InstallmentDto::fromEntity).collect(Collectors.toSet()))
                .build();
    }
}
