package com.kostenko.demo.proxy.seller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPageDTO {
    @Schema(description = "Unique identifier of the user", example = "65c116123567ba2bbb20a717")
    private String id;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "List of posts made by the user")
    private Set<SimplePostDTO> posts;

    @Schema(description = "List of comments made by the user")
    private Set<CommentDTO> comments;

    @Schema(description = "List of likes made by the user")
    private Set<LikeDTO> likes;

    @Schema(description = "List of followers of the user")
    private Set<SimpleUserDTO> followers;

    @Schema(description = "List of users that the user follows")
    private Set<SimpleUserDTO> following;
}