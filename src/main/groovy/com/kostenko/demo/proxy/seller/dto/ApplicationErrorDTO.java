package com.kostenko.demo.proxy.seller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationErrorDTO {
    @Schema(description = "Status code of the error", example = "404")
    private int statusCode;

    @Schema(description = "Error message", example = "Resource not found")
    private String message;
}