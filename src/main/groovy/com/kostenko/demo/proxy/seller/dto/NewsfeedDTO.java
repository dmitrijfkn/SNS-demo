package com.kostenko.demo.proxy.seller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsfeedDTO {
    @Schema(description = "List of posts in the newsfeed")
    private Set<PostDTO> posts;

    @Schema(description = "List of likes in the newsfeed")
    private Set<LikeDTO> likes;

    @Schema(description = "List of comments in the newsfeed")
    private Set<CommentDTO> comments;
}
