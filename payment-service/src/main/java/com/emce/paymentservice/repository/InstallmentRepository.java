package com.emce.paymentservice.repository;

import com.emce.commons.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentRepository extends JpaRepository<Installment, Integer> {
}
