package com.emce.creditsservice.service;

import com.emce.creditsservice.dto.CreditRequest;
import com.emce.creditsservice.entity.Credit;
import com.emce.creditsservice.entity.Installment;
import com.emce.creditsservice.entity.InstallmentStatus;
import com.emce.creditsservice.entity.Status;
import com.emce.creditsservice.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditRepository creditRepository;

    public Credit createCredit(CreditRequest request) {
        LocalDate now = LocalDate.now();
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

        // Use BigDecimal for precise calculations
        BigDecimal total = BigDecimal.valueOf(totalAmount);
        BigDecimal count = BigDecimal.valueOf(installmentCount);

        // Calculate the base installment amount (rounded to 2 decimal places)
        BigDecimal baseAmount = total.divide(count, 2, RoundingMode.DOWN);

        // Calculate the remainder to distribute
        BigDecimal remainder = total.subtract(baseAmount.multiply(count));

        var now = LocalDate.now();

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
                    .deadline(now.plusMonths(i + 1))
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
            installments.add(installment);
        }

        return installments;
    }

}
