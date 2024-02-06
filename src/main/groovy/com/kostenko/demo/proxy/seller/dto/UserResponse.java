package com.kostenko.demo.proxy.seller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {
    @Schema(description = "Unique identifier of the user", example = "65c116123567ba2bbb20a717")
    private String id;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;
}
