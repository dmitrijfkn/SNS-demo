package com.kostenko.demo.proxy.seller.dto;

import lombok.*;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsfeedDTO {
    Set<PostDTO> posts;

    Set<LikeDTO> likes;

    Set<CommentDTO> comments;
}
