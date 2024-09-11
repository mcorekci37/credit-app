package com.emce.authserver.dto;

import com.emce.authserver.entity.Role;
import com.emce.authserver.entity.UserCredential;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record UserResponse(Integer id, String firstName, String lastName, String email,
                           LocalDateTime createdAt, LocalDateTime updatedAt, Role role) {
    public static UserResponse fromEntity(UserCredential userCredential) {
        return UserResponse.builder()
                .id(userCredential.getId())
                .firstName(userCredential.getFirstName())
                .lastName(userCredential.getLastName())
                .email(userCredential.getEmail())
                .createdAt(userCredential.getCreatedAt())
                .updatedAt(userCredential.getUpdatedAt())
                .role(userCredential.getRole())
                .build();
    }
}
