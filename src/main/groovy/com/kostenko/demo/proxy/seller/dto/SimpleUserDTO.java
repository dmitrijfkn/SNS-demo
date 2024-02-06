package com.kostenko.demo.proxy.seller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserDTO {
    @Schema(description = "Unique identifier of the user", example = "65c116123567ba2bbb20a717")
    private String id;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;
}
