package com.oleksa.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Token refresh request")
public class TokenRefreshRequest {

    @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    @Size(min = 20, message = "Refresh token length must be at least 20 characters")
    @NotBlank(message = "Refresh token cannot be empty")
    private String refreshToken;
}