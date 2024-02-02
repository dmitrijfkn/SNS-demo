package com.kostenko.demo.proxy.seller.service;

import com.kostenko.demo.proxy.seller.dto.CommentDTO;
import com.kostenko.demo.proxy.seller.dto.PostDTO;
import com.kostenko.demo.proxy.seller.entity.Comment;
import com.kostenko.demo.proxy.seller.entity.Post;
import com.kostenko.demo.proxy.seller.entity.User;
import com.kostenko.demo.proxy.seller.error.ResourceNotFoundException;
import com.kostenko.demo.proxy.seller.repository.CommentRepository;
import com.kostenko.demo.proxy.seller.repository.PostRepository;
import com.kostenko.demo.proxy.seller.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing posts, providing methods for post-related operations.
 */
@Service
public class PostService {

    /**
     * Constant error message template for resource not found exceptions.
     * The placeholder %d is intended to be replaced with the specific post identifier.
     */
    protected static String ID_NOT_FOUND_MESSAGE = "Post with id: \"%d\" doesn't exist.";
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

    /**
     * Constructs a PostService with the specified repositories and model mapper.
     *
     * @param postRepository        Repository for managing Post entities.
     * @param userRepository        Repository for managing User entities.
     * @param commentRepository     Repository for managing Comment entities.
     * @param modelMapper           Mapper for converting entities to DTOs and vice versa.
     */

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new post with the given content and associates it with the user identified by the specified userId.
     *
     * @param userId  The unique identifier of the user who is the author of the post.
     * @param content The content of the post.
     * @return {@link com.kostenko.demo.proxy.seller.dto.PostDTO} object representing the newly created post.
     * @throws ResourceNotFoundException - if the user with the given userId is not found in the database.
     */
    @Transactional
    public PostDTO createPost(String userId,
                              String content) {
        User postAuthor = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(UserService.ID_NOT_FOUND_MESSAGE, userId)));

        Post post = Post.builder()
                .content(content)
                .user(postAuthor)
                .build();

        postAuthor.addPost(post);

        postRepository.save(post);

        return modelMapper.map(post, PostDTO.class);
    }

    /**
     * The deletePost function deletes a post with certain id from the database.
     * If post with requested id doesn't exist, silently ignores request
     *
     * @param postId id of a post to be deleted
     * @param userId user requested deletion, must be an author in order to delete a post
     * @throws AccessDeniedException - if user requested deletion not an author of a post
     */
    public void deletePost(String postId, String userId) {
        Post post = postRepository.findById(postId).orElse(null);

        if (post == null) {
            return;
        }

        if (!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException(UserService.ACCESS_DENIED_MESSAGE);
        }

        postRepository.deleteById(postId);
    }

    /**
     * Creates a new comment with the given content, associates it with the user identified by the specified userId
     * and post identified by the specified postId.
     *
     * @param userId  The unique identifier of the user who is the author of the comment.
     * @param content The content of the comment.
     * @param postId  The content of the post.
     *
     * @return {@link com.kostenko.demo.proxy.seller.dto.CommentDTO} object representing the newly created comment.
     *
     * @throws ResourceNotFoundException - if the user or post with the given userId is not found in the database.
     */
    public CommentDTO createComment(String userId,
                                 String content,
                                 String postId) {

        User commentAuthor = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(UserService.ID_NOT_FOUND_MESSAGE, userId)));

        Post commentedPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND_MESSAGE, userId)));

        Comment comment = Comment.builder()
                .content(content)
                .user(commentAuthor)
                .post(commentedPost)
                .build();

        commentAuthor.addComment(comment);
        commentedPost.addComment(comment);

        commentRepository.save(comment);

        return modelMapper.map(comment, CommentDTO.class);
    }
}
