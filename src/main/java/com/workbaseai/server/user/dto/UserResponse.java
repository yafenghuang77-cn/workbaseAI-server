package com.workbaseai.server.user.dto;

import java.time.Instant;

public record UserResponse(long id, String username, String displayName, String email, int status,
        Instant createdAt, Instant updatedAt) {
}
