package com.kostenko.demo.proxy.seller.dto;

import lombok.*;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private String id;

    private String content;

   // private SimpleUserDTO user;

    private Instant createdAt;

    private Instant updatedAt;
}
