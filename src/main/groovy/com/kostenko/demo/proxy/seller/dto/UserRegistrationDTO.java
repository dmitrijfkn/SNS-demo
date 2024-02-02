package com.kostenko.demo.proxy.seller.dto;

import jakarta.validation.constraints.Email;
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

    @Email(message = "Email field should contain valid email")
    @NotEmpty(message = "Email field is required.")
    @Size(min = 2, max = 255, message = "The length of email must be between 2 and 255 characters.")
    private String username;


    @NotEmpty(message = "Password field is required.")
    @Size(min = 8, max = 255, message = "The length of password must be between 8 and 255 characters.")
    private String password;


    @NotEmpty(message = "Name field is required.")
    @Size(min = 2, max = 255, message = "The length of name must be between 2 and 255 characters.")
    private String name;


    @NotEmpty(message = "Surname field is required.")
    @Size(min = 2, max = 255, message = "The length of surname must be between 2 and 255 characters.")
    private String surname;


    @NotEmpty(message = "PhoneNumber field is required.")
    @Size(min = 8, max = 12, message = "The length of phoneNumber must be between 8 and 12 characters.")
    private String phoneNumber;
}