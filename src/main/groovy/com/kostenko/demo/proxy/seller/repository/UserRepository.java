package com.kostenko.demo.proxy.seller.repository;

import com.kostenko.demo.proxy.seller.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link com.kostenko.demo.proxy.seller.entity.User} entities in MongoDB.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The user with the specified username, or null if not found.
     */
    User findByUsername(String username);

    /**
     * Checks if a user with the specified username exists in the repository.
     *
     * @param username The username to check for existence.
     * @return true if a user with the specified username exists, false otherwise.
     */
    boolean existsByUsername(String username);
}