package com.kostenko.demo.proxy.seller.dto;

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
    private String id;

    private String content;

    private SimplePostDTO postDTO;

    private SimpleUserDTO postCreator;

    private Instant createdAt;

    private Instant updatedAt;
}
