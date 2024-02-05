package com.kostenko.demo.proxy.seller.service;


import com.kostenko.demo.proxy.seller.entity.RefreshToken;
import com.kostenko.demo.proxy.seller.repository.RefreshTokenRepository;
import com.kostenko.demo.proxy.seller.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${jwt.refreshTokenExpiry}")
    private int refreshTokenExpiry;


    /**
     * Scheduled task to automatically delete expired refresh tokens from the repository.
     * This task runs daily at midnight (00:00:00) based on the cron expression "0 0 0 * * *".
     * Expired refresh tokens are identified and removed to ensure the repository remains
     * free of outdated records.
     */
    @Scheduled(cron = "0 0 0 * * *") // Delete outdated tokens every day at midnight
    private void deleteExpiredTokens() {
        //       refreshTokenRepository.deleteExpired();
    }  //TODO fix and uncomment


    /**
     * Creates a new refresh token for the specified user and saves it to the repository.
     * The refresh token is associated with the user identified by the provided username.
     * It generates a random token, sets an expiration date based on the configured in
     * jwt.refreshTokenExpiry in application properties
     * expiration duration, and saves the refresh token in the repository.
     *
     * @param userId - id of the user for whom the refresh token is created.
     * @return {@link RefreshToken refreshToken}
     * - The newly created refresh token associated with the user.
     */
    public RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByUsername(userId))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(refreshTokenExpiry))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }

        return token;
    }

}
