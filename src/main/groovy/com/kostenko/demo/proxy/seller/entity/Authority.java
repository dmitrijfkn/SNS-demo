package com.kostenko.demo.proxy.seller.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Document
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {
    @Id
    private String id;

    private String authority;

    @DBRef
    private Set<User> users;
}
