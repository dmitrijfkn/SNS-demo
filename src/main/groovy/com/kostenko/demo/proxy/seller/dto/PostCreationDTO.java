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
public class PostCreationDTO {
    @NotNull(message = "Content cannot be null")
    @NotBlank(message = "Content cannot be blank")
    @NotEmpty(message = "Content cannot be empty")
    @Size(min = 1, max = 255, message = "Content length must be between 1 and 255 characters")
    @Schema(description = "Content of the post", example = "This is a post content")
    private String content;
}