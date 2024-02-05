package com.kostenko.demo.proxy.seller.repository;

import com.kostenko.demo.proxy.seller.entity.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends MongoRepository<Like, String> {
    Like findByLikeCreatorIdAndPostId(String userId, String postId);
}