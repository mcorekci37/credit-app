package com.emce.creditsservice.dto;

import com.emce.creditsservice.entity.Credit;
import lombok.Builder;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record CreditResponse(Integer userId, Integer creditId, Set<InstallmentDto> installments) {
    public static CreditResponse fromEntity(Credit credit) {
        return CreditResponse.builder()
                .userId(credit.getUserId())
                .creditId(credit.getId())
                .installments(credit.getInstallments().stream().map(InstallmentDto::fromEntity).collect(Collectors.toSet()))
                .build();
    }
}
