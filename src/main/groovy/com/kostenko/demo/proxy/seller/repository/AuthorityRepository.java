package com.kostenko.demo.proxy.seller.repository;

import com.kostenko.demo.proxy.seller.entity.Authority;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface AuthorityRepository extends MongoRepository<Authority, String> {

    Authority findByAuthority(String authority);
}