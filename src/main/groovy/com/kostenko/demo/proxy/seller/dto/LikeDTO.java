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
public class LikeDTO {
    @Schema(description = "Unique identifier of the like", example = "65c116123567ba2bbb20a717")
    private String id;

    @Schema(description = "Post that was liked")
    private SimplePostDTO post;

    @Schema(description = "User who created the like")
    private SimpleUserDTO likeCreator;
}
