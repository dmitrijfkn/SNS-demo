package com.kostenko.demo.proxy.seller.repository;

import com.kostenko.demo.proxy.seller.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface CommentRepository extends MongoRepository<Comment, String> {

}