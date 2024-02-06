package com.kostenko.demo.proxy.seller.repository;

import com.kostenko.demo.proxy.seller.entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RepositoryRestResource(exported = false)
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

}
