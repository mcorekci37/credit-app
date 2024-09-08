package com.emce.creditsservice.service;

import com.emce.creditsservice.dto.CreditRequest;
import com.emce.creditsservice.entity.Credit;
import com.emce.creditsservice.entity.Installment;
import com.emce.creditsservice.entity.InstallmentStatus;
import com.emce.creditsservice.entity.Status;
import com.emce.creditsservice.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
//import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditRepository creditRepository;

    @Transactional
    public Credit createCredit(CreditRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Credit credit = Credit.builder()
                .status(Status.APPROVED)
                .amount(request.amount())
                .userId(request.userId())
                .createdAt(now)
                .updatedAt(now)
                .build();


        // Set installments
        Set<Installment> installments = createInstallments(request.amount(), request.installmentCount());
        credit.setInstallments(installments);

        credit = creditRepository.save(credit);
        return credit;
    }


    private Set<Installment> createInstallments(Double totalAmount, Integer installmentCount) {
        Set<Installment> installments = new HashSet<>();

        BigDecimal total = BigDecimal.valueOf(totalAmount);
        BigDecimal count = BigDecimal.valueOf(installmentCount);

        BigDecimal baseAmount = total.divide(count, 2, BigDecimal.ROUND_DOWN);

        BigDecimal remainder = total.subtract(baseAmount.multiply(count));

        var now = LocalDateTime.now();


        // Distribute the base amount and remainder
        for (int i = 0; i < installmentCount; i++) {
            BigDecimal installmentAmount = baseAmount;

            // Distribute the remainder over the first few installments
            if (remainder.compareTo(BigDecimal.ZERO) > 0) {
                installmentAmount = installmentAmount.add(BigDecimal.valueOf(0.01));
                remainder = remainder.subtract(BigDecimal.valueOf(0.01));
            }

            var installment = Installment.builder()
                    .amount(installmentAmount.doubleValue())  // Convert back to double for the entity
                    .status(InstallmentStatus.DEPTOR)
                    .deadline(adjustToWeekday(LocalDate.now().plusMonths(i + 1)))
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
            installments.add(installment);
        }

        return installments;
    }

    public List<Credit> listCreditsForUser(Integer userId) {
        return creditRepository.findByUserId(userId);

    }

    public static LocalDate adjustToWeekday(LocalDate date) {
        // Check if the date is Saturday or Sunday
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return date.plusDays(2);
        } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return date.plusDays(1);
        } else {
            return date;
        }
    }
}
