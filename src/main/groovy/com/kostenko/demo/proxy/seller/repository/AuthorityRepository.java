package com.kostenko.demo.proxy.seller.repository;

import com.kostenko.demo.proxy.seller.entity.Authority;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends MongoRepository<Authority, String> {

    Authority findByAuthority(String authority);
}