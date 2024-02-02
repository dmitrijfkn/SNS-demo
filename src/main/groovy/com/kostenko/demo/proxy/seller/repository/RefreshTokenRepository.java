package com.kostenko.demo.proxy.seller.repository;

import com.kostenko.demo.proxy.seller.entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

/*    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshToken e WHERE e.expiryDate < CURRENT_TIMESTAMP")
    void deleteExpired();*/
}
