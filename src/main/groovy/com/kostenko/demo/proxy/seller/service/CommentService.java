package com.kostenko.demo.proxy.seller.service;

import com.kostenko.demo.proxy.seller.dto.CommentDTO;
import com.kostenko.demo.proxy.seller.entity.Comment;
import com.kostenko.demo.proxy.seller.entity.Post;
import com.kostenko.demo.proxy.seller.entity.User;
import com.kostenko.demo.proxy.seller.error.ResourceNotFoundException;
import com.kostenko.demo.proxy.seller.repository.CommentRepository;
import com.kostenko.demo.proxy.seller.repository.PostRepository;
import com.kostenko.demo.proxy.seller.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CommentService {
    /**
     * Repository for managing {@link com.kostenko.demo.proxy.seller.entity.Post} entities in MongoDB.
     */
    private final PostRepository postRepository;
    /**
     * Repository for managing {@link com.kostenko.demo.proxy.seller.entity.User} entities in MongoDB.
     */
    private final UserRepository userRepository;
    /**
     * Repository for managing {@link com.kostenko.demo.proxy.seller.entity.Comment} entities in MongoDB.
     */
    private final CommentRepository commentRepository;
    /**
     * Mapper for converting entities to DTOs and vice versa.
     */
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CommentService(PostRepository postRepository,
                          UserRepository userRepository,
                          CommentRepository commentRepository,
                          ModelMapper modelMapper,
                          MongoTemplate mongoTemplate) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.mongoTemplate = mongoTemplate;
    }


    /**
     * Creates a new comment with the given content, associates it with the user identified by the specified userId
     * and post identified by the specified postId.
     *
     * @param userId  The unique identifier of the user who is the author of the comment.
     * @param content The content of the comment.
     * @param postId  The content of the post.
     * @return {@link com.kostenko.demo.proxy.seller.dto.CommentDTO} object representing the newly created comment.
     * @throws ResourceNotFoundException - if the user or post with the given userId is not found in the database.
     */
    @Transactional
    public CommentDTO createComment(String userId,
                                    String content,
                                    String postId) {

        User commentAuthor = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(UserService.ID_NOT_FOUND_MESSAGE, userId)));

        Post commentedPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(PostService.ID_NOT_FOUND_MESSAGE, postId)));

        Comment comment = Comment.builder()
                .content(content)
                .user(commentAuthor)
                .post(commentedPost)
                .build();

        commentRepository.save(comment);

        addCommentToUser(userId, comment);
        addCommentToPost(postId, comment);


        return modelMapper.map(comment, CommentDTO.class);
    }


    protected void addCommentToUser(String userId, Comment comment) {
        Query query = Query.query(Criteria.where("_id").is(userId));
        Update update = new Update().addToSet("comments", comment);
        mongoTemplate.updateFirst(query, update, User.class);
    }


    protected void addCommentToPost(String postId, Comment comment) {
        Query query = Query.query(Criteria.where("_id").is(postId));
        Update update = new Update().addToSet("comments", comment);
        mongoTemplate.updateFirst(query, update, Post.class);
    }


}
