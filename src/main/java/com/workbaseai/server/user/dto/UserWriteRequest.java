package com.workbaseai.server.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserWriteRequest(
        @NotBlank @Size(max = 64) String username,
        @NotBlank @Size(max = 100) String displayName,
        @Email @Size(max = 254) String email,
        @Min(0) @Max(1) Integer status) {
}
