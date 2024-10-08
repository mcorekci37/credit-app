package com.emce.creditsservice.service;

import com.emce.creditsservice.config.UserClient;
import com.emce.creditsservice.dto.CreditRequest;
import com.emce.creditsservice.exception.CreditNotFoundException;
import com.emce.commons.entity.Credit;
import com.emce.commons.entity.Installment;
import com.emce.commons.entity.InstallmentStatus;
import com.emce.commons.entity.Status;
import com.emce.commons.exception.UserNotFoundException;
import com.emce.creditsservice.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
//import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreditService {
    public static final String CREDIT_NOT_FOUND_MSG = "Credit not found for id %s";
    public static final String USER_NOT_FOUND_MSG = "User not found for id %s";
    private final CreditRepository creditRepository;
    private final UserClient userClient;

    @Transactional
    public Credit createCredit(CreditRequest request) {
        if (!userClient.checkExistsById(request.userId())) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, request.userId()));
        }
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
                    .dept(installmentAmount.doubleValue())  // Convert back to double for the entity
                    .interest(0D)
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
    public Page<Credit> listCreditsForUser(Integer userId, String statusStr, LocalDate fromDate, LocalDate toDate, int page, int size, String sortBy) {
        if (fromDate==null && toDate==null){
            return this.listCreditsForUser(userId, statusStr, page, size, sortBy);
        }else {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            Status status = Status.getStatus(statusStr);
            LocalDateTime from = fromDate!=null ? fromDate.atStartOfDay() : null;
            LocalDateTime to = toDate!=null ? toDate.atTime(LocalTime.MAX) : null;
            return creditRepository.findByUserIdAndFilters(userId, status, from, to, pageable);
        }
    }
    public Page<Credit> listCreditsForUser(Integer userId, String statusStr, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Status status = Status.getStatus(statusStr);
        if (status !=null){
            return creditRepository.findByUserIdAndStatus(userId, status, pageable);
        }else {
            return creditRepository.findByUserId(userId, pageable);
        }
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

    public Credit getCredit(Integer creditId) {
        Credit credit = creditRepository.findById(creditId).orElseThrow(
                () -> new CreditNotFoundException(String.format(CREDIT_NOT_FOUND_MSG, creditId)));
        return credit;
    }
}
