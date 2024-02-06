package com.kostenko.demo.proxy.seller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDTO {

    @NotEmpty(message = "Username field is required.")
    @Size(min = 2, max = 255, message = "The length of username must be between 2 and 255 characters.")
    private String username;


    @NotEmpty(message = "Password field is required.")
    @Size(min = 8, max = 255, message = "The length of password must be between 8 and 255 characters.")
    private String password;
}