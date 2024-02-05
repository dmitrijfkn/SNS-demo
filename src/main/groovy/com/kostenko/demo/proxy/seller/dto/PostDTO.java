package com.kostenko.demo.proxy.seller.dto;

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

    private String id;

    private String content;

    private SimpleUserDTO postCreator;

    private Set<CommentDTO> comments;

    private Set<LikeDTO> likes;

    private Instant createdAt;

    private Instant updatedAt;

    private int likeCount;
}
