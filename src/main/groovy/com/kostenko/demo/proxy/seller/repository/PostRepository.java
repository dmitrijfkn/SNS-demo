package com.kostenko.demo.proxy.seller.repository;

import com.kostenko.demo.proxy.seller.entity.Post;
import com.kostenko.demo.proxy.seller.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface PostRepository extends MongoRepository<Post, String> {
    void deleteAllByPostCreator(User user);
}