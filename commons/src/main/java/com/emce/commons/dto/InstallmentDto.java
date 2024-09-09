package com.emce.commons.dto;

import com.emce.commons.entity.Installment;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;

@Builder
public record InstallmentDto(Integer id, LocalDate dueDate, Double amount, Double dept, Double interest, String status) {
    public static InstallmentDto fromEntity(Installment installment) {
        return InstallmentDto.builder()
                .id(installment.getId())
                .dueDate(installment.getDeadline())
                .amount(installment.getAmount())
                .dept(installment.getDept())
                .interest(installment.getInterest())
                .status(installment.getStatus().name())
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
