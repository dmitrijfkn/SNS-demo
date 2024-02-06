package com.kostenko.demo.proxy.seller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    @Schema(description = "Unique identifier of the post", example = "65c116123567ba2bbb20a717")
    private String id;

    @Schema(description = "Content of the post", example = "This is a post content")
    private String content;

    @Schema(description = "User who created the post")
    private SimpleUserDTO postCreator;

    @Schema(description = "List of comments on the post")
    private Set<CommentDTO> comments;

    @Schema(description = "List of likes on the post")
    private Set<LikeDTO> likes;

    @Schema(description = "Timestamp when the post was created")
    private Instant createdAt;

    @Schema(description = "Timestamp when the post was last updated")
    private Instant updatedAt;

    @Schema(description = "Number of likes for the post")
    private int likeCount;
}