package com.kostenko.demo.proxy.seller.dto;

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
    private String id;

    private String username;

    private Set<SimplePostDTO> posts;

    private Set<CommentDTO> comments;

    private Set<LikeDTO> likes;

    private Set<SimpleUserDTO> followers;

    private Set<SimpleUserDTO> following;
}