package com.emce.creditsservice.repository;

import com.emce.creditsservice.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Integer> {
}
