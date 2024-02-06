package com.kostenko.demo.proxy.seller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    @Schema(description = "Unique identifier of the comment", example = "65c116123567ba2bbb20a717")
    private String id;

    @Schema(description = "Content of the comment", example = "This is a comment content")
    private String content;

    @Schema(description = "Post to which the comment belongs")
    private SimplePostDTO postDTO;

    @Schema(description = "User who created the comment")
    private SimpleUserDTO postCreator;

    @Schema(description = "Timestamp when the comment was created")
    private Instant createdAt;

    @Schema(description = "Timestamp when the comment was last updated")
    private Instant updatedAt;
}