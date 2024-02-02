package com.kostenko.demo.proxy.seller.dto;

import com.kostenko.demo.proxy.seller.entity.Post;
import com.kostenko.demo.proxy.seller.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private String id;

    private String content;

    private SimpleUserDTO user;

    private Set<CommentDTO> comments;

    private Set<LikeDTO> likes;

    private Instant createdAt;

    private Instant updatedAt;

    private int likeCount;
}
