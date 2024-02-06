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
public class SimplePostDTO {
    @Schema(description = "Unique identifier of the post", example = "65c116123567ba2bbb20a717")
    private String id;

    @Schema(description = "Content of the post", example = "This is a post content")
    private String content;

    @Schema(description = "Timestamp when the post was created")
    private Instant createdAt;

    @Schema(description = "Timestamp when the post was last updated")
    private Instant updatedAt;

    @Schema(description = "Number of likes for the post")
    private int likeCount;
}