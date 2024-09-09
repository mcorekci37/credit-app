package com.emce.paymentservice.service;

import com.emce.commons.dto.CreditResponse;
import com.emce.commons.entity.Installment;
import com.emce.commons.entity.InstallmentStatus;
import com.emce.paymentservice.dto.PaymentRequest;
import com.emce.paymentservice.exception.InstallmentNotFoundException;
import com.emce.paymentservice.repository.InstallmentRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final InstallmentRepository repository;

    @Transactional
    public CreditResponse pay(PaymentRequest request) throws NotFoundException {
        Installment installment = repository.findById(request.installmentId()).orElseThrow(
                () -> new InstallmentNotFoundException(String.format("Installment with %s id not found", request.installmentId())));
        if (installment.getDept().equals(request.amount())) {
            installment.setStatus(InstallmentStatus.AMORTIZED);
            installment.setDept(0D);
        } else if (Double.compare(installment.getDept(),request.amount())>0) {
            installment.setStatus(InstallmentStatus.PARTIALLY_AMORTIZED);
            installment.setDept(installment.getDept()-request.amount());
        } else if (Double.compare(installment.getDept(),request.amount())<0) {
            installment.setStatus(InstallmentStatus.AMORTIZED);
            installment.setDept(0D);
            //todo trying to pay more than the dept. to be handled carefully
        }
        installment = repository.saveAndFlush(installment);
        return CreditResponse.fromEntity(installment.getCredit());
    }
}
