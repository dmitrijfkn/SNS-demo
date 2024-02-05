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
public class SimplePostDTO {

    private String id;

    private String content;

    private Instant createdAt;

    private Instant updatedAt;

    private int likeCount;
}
