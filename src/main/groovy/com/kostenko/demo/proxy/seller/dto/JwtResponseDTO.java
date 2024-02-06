package com.kostenko.demo.proxy.seller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDTO {
    @Schema(description = "Access token for authentication", example = "eyJhbGcUzI1NiJ9.eyJzdWZiOTM1NjdiOTJiYmIyMGE3MT3MDcyMzU2MDksImV4cCI6MTcwODUzMTYwOX0.6_BKkSKHb7-aZA5CbSMbnxTZcexi1192seQis")
    private String accessToken;

    @Schema(description = "JWT refresh token", example = "2f81db-8779-4f-9bca-ca8e1036f")
    private String refreshToken;
}
