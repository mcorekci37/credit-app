package com.emce.creditsservice.dto;

import com.emce.creditsservice.entity.Installment;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;

@Builder
public record InstallmentDto(Integer id, LocalDate dueDate, Double amount) {
    public static InstallmentDto fromEntity(Installment installment) {
        return InstallmentDto.builder()
                .id(installment.getId())
                .dueDate(installment.getDeadline())
                .amount(installment.getAmount())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstallmentDto that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(dueDate, that.dueDate) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dueDate, amount);
    }
}
