package com.kostenko.demo.proxy.seller.dto;


import com.kostenko.demo.proxy.seller.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequest {

    private Long id;
    private String email;
    private String password;
    private Set<Authority> roles;

}
