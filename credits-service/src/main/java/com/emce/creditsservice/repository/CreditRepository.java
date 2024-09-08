package com.emce.creditsservice.repository;

import com.emce.creditsservice.entity.Credit;
import com.emce.creditsservice.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Integer> {
    List<Credit> findByUserId(Integer userId);
    Page<Credit> findByUserId(Integer userId, Pageable pageable);
    Page<Credit> findByUserIdAndStatus(Integer userId, Status status, Pageable pageable);
    @Query("SELECT c FROM Credit c WHERE c.userId = :userId " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (cast(:fromDate as timestamp) IS NULL OR c.createdAt >= :fromDate) " +
            "AND (cast(:toDate as timestamp) IS NULL OR c.createdAt <= :toDate)")
    Page<Credit> findByUserIdAndFilters(
            @Param("userId") Integer userId,
            @Param("status") Status status,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );
}
