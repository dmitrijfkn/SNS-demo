package com.kostenko.demo.proxy.seller.repository;

import com.kostenko.demo.proxy.seller.entity.Post;
import com.kostenko.demo.proxy.seller.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    void deleteAllByPostCreator(User user);

    Set<Post> findAllByPostCreatorFollowersIdContains(String postCreator_followers_id);

    Set<Post> findPostByPostCreator_Followers_IdContains(String userId);

    Set<Post> findAllByPostCreator_FollowersContainsOrderByCreatedAtAsc(User user);
}