package com.emce.creditsservice.dto;

import com.emce.creditsservice.entity.Installment;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record InstallmentDto(Integer id, LocalDate dueDate, Double amount) {
    public static InstallmentDto fromInstallment(Installment installment) {
        return InstallmentDto.builder()
                .id(installment.getId())
                .dueDate(installment.getDeadline())
                .amount(installment.getAmount())
                .build();
    }
}
