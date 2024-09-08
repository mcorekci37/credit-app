package com.emce.authserver.repository;

import com.emce.authserver.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserCredential, Integer> {
    Optional<UserCredential> findByEmail(String email);
}
