package com.kostenko.demo.proxy.seller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCreationDTO {
    @NotNull(message = "Content cannot be null")
    @NotBlank(message = "Content cannot be blanc")
    @NotEmpty(message = "Content cannot be empty")
    @Schema(description = "Content of the comment", example = "This is a comment content")
    private String content;

    @NotNull(message = "Post ID cannot be null")
    @NotBlank(message = "Post ID cannot be blanc")
    @NotEmpty(message = "Post ID cannot be empty")
    @Schema(description = "ID of the post to which the comment belongs", example = "123456")
    private String postId;
}
