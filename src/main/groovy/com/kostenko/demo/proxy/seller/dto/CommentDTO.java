package com.kostenko.demo.proxy.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private String id;

    private String content;

    private SimpleUserDTO user;

    private Instant createdAt;

    private Instant updatedAt;
}
