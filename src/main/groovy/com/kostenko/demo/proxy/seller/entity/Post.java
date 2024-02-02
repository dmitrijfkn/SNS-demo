package com.kostenko.demo.proxy.seller.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;


@Document
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    private String id;

    private String content;

    @DBRef(lazy = true)
    private User user;

    @DBRef
    private Set<Comment> comments;

    @DBRef
    private Set<Like> likes;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public int getLikeCount() {
        return likes != null ? likes.size() : 0;
    }

    public void addComment(Comment comment) {
        if (this.comments == null) {
            this.comments = Collections.emptySet();
        }

        this.comments.add(comment);
    }
}
