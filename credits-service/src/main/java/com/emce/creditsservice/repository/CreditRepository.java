package com.emce.creditsservice.repository;

import com.emce.creditsservice.entity.Credit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Integer> {
    List<Credit> findByUserId(Integer userId);
    Page<Credit> findByUserId(Integer userId, Pageable pageable);
}
