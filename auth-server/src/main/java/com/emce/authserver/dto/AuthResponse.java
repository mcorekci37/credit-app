package com.emce.authserver.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record AuthResponse(String token, Date expiresAt) {
}
