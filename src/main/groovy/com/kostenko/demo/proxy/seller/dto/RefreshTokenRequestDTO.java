package com.kostenko.demo.proxy.seller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDTO {
    @Schema(description = "Refresh token to be used for token refresh", example = "2f81db-8779-4f-9bca-ca8e1036f")
    private String token;
}