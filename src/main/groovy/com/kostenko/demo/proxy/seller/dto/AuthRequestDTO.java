package com.kostenko.demo.proxy.seller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestDTO {
    @NotNull(message = "Username field is required.")
    @NotBlank(message = "Username field is required.")
    @NotEmpty(message = "Username field is required.")
    @Size(min = 2, max = 32, message = "The length of username must be between 2 and 32 characters.")
    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @NotNull(message = "Password field is required.")
    @NotBlank(message = "Password field is required.")
    @NotEmpty(message = "Password field is required.")
    @Size(min = 8, max = 64, message = "The length of password must be between 8 and 64 characters.")
    @Schema(description = "Password of the user", example = "password123")
    private String password;
}