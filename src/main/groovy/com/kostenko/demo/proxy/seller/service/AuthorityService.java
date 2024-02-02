package com.kostenko.demo.proxy.seller.service;

import com.kostenko.demo.proxy.seller.entity.Authority;
import com.kostenko.demo.proxy.seller.entity.User;
import com.kostenko.demo.proxy.seller.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;


    //TODO Убрать этот кошмар!!!
    Authority admin;
    Authority librarian;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
        this.admin = authorityRepository.findByAuthority("ROLE_ADMIN");
        this.librarian = authorityRepository.findByAuthority("ROLE_LIBRARIAN");
    }


    public boolean isLibrarianOrAdmin(User user) {

        return user.getAuthorities().contains(admin)
                || user.getAuthorities().contains(librarian);

    }
}