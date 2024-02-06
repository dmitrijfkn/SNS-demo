package com.kostenko.demo.proxy.seller.service;

import com.kostenko.demo.proxy.seller.dto.*;
import com.kostenko.demo.proxy.seller.entity.Comment;
import com.kostenko.demo.proxy.seller.entity.Like;
import com.kostenko.demo.proxy.seller.entity.Post;
import com.kostenko.demo.proxy.seller.entity.User;
import com.kostenko.demo.proxy.seller.error.ResourceNotFoundException;
import com.kostenko.demo.proxy.seller.repository.LikeRepository;
import com.kostenko.demo.proxy.seller.repository.PostRepository;
import com.kostenko.demo.proxy.seller.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing posts, providing methods for post-related operations.
 */
@Service
public class PostService {

    /**
     * Constant error message template for resource not found exceptions.
     * The placeholder %d is intended to be replaced with the specific post identifier.
     */
    protected static final String ID_NOT_FOUND_MESSAGE = "Post with id: \"%s\" doesn't exist.";
    /**
     * Repository for managing {@link com.kostenko.demo.proxy.seller.entity.Post} entities in MongoDB.
     */
    private final PostRepository postRepository;
    /**
     * Repository for managing {@link com.kostenko.demo.proxy.seller.entity.User} entities in MongoDB.
     */
    private final UserRepository userRepository;
    /**
     * Repository for managing {@link com.kostenko.demo.proxy.seller.entity.Like} entities in MongoDB.
     */
    private final LikeRepository likeRepository;
    /**
     * Mapper for converting entities to DTOs and vice versa.
     */
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;


    /**
     * Constructs a PostService with the specified repositories and model mapper.
     *
     * @param postRepository Repository for managing Post entities.
     * @param userRepository Repository for managing User entities.
     * @param modelMapper    Mapper for converting entities to DTOs and vice versa.
     */

    @Autowired
    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       LikeRepository likeRepository,
                       ModelMapper modelMapper,
                       MongoTemplate mongoTemplate
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.modelMapper = modelMapper;
        this.mongoTemplate = mongoTemplate;
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
                .postCreator(postAuthor)
                .build();

        postRepository.save(post);

        Query query = Query.query(Criteria.where("_id").is(userId));
        Update update = new Update().addToSet("posts", post);
        mongoTemplate.updateFirst(query, update, User.class);

        return modelMapper.map(post, PostDTO.class);
    }


    public PostDTO editPost(String postId, PostCreationDTO postDTO, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND_MESSAGE, postId)));

        if (!post.getPostCreator().getId().equals(userId)) {
            throw new AccessDeniedException(UserService.ACCESS_DENIED_MESSAGE);
        }

        if (!post.getContent().equals(postDTO.getContent())) {
            post.setContent(postDTO.getContent());
        }
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

        if (!post.getPostCreator().getId().equals(userId)) {
            throw new AccessDeniedException(UserService.ACCESS_DENIED_MESSAGE);
        }

        postRepository.deleteById(postId);
    }


    /**
     * The savePostAsFavorite function saves post to user favorites.
     *
     * @param postId id of a post to be deleted
     * @param userId user requested, to his favorites post will be saved
     * @throws ResourceNotFoundException - if the user or post with the given userId is not found in the database.
     */
    public void addPostToFavorites(String userId, String postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND_MESSAGE, postId));
        }
        // Perform a partial update to add the new favorite post
        Query query = Query.query(Criteria.where("_id").is(userId));
        Update update = new Update().addToSet("favoritePosts", postId);
        mongoTemplate.updateFirst(query, update, User.class);
    }


    /**
     * The deletePostFromFavorite function deletes a post from user favorites.
     * If post with requested id doesn't exist in favorites, silently ignores request
     *
     * @param postId id of a post to be deleted from favorites
     * @param userId user requested deletion
     * @throws ResourceNotFoundException - if the user with the given userId is not found in the database.
     */
    public void removePostFromFavorites(String userId, String postId) {
        // Perform a partial update to remove the specified favorite post
        Query query = Query.query(Criteria.where("_id").is(userId));
        Update update = new Update().pull("favoritePosts", postId);
        mongoTemplate.updateFirst(query, update, User.class);
    }


    @Transactional
    public void addLikeToPost(String userId, String postId) {
        Post postToLike = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND_MESSAGE, postId)));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(UserService.ID_NOT_FOUND_MESSAGE, userId)));

        Like like = Like.builder()
                .post(postToLike)
                .likeCreator(user)
                .build();

        likeRepository.save(like);

        // Add like to post
        Query query = Query.query(Criteria.where("_id").is(postId));
        Update update = new Update().addToSet("likes", like.getId());
        mongoTemplate.updateFirst(query, update, Post.class);

        // Add like to User
        query = Query.query(Criteria.where("_id").is(userId));
        update = new Update().addToSet("likes", like.getId());
        mongoTemplate.updateFirst(query, update, User.class);
    }


    @Transactional
    public void removeLikeFromPost(String userId, String postId) {
        Like like = likeRepository.findByLikeCreatorIdAndPostId(userId, postId);

        // Remove like from post
        Query query = Query.query(Criteria.where("_id").is(postId));
        Update update = new Update().pull("likes", like.getId());
        mongoTemplate.updateFirst(query, update, Post.class);

        // Remove like from User
        query = Query.query(Criteria.where("_id").is(userId));
        update = new Update().pull("likes", like.getId());
        mongoTemplate.updateFirst(query, update, User.class);

        likeRepository.delete(like);
    }

    public NewsfeedDTO newsfeed(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(UserService.ID_NOT_FOUND_MESSAGE, userId)));

        Set<PostDTO> posts = user
                .getFollowing()
                .stream()
                .flatMap(following -> following.getPosts().stream())
                .sorted(Comparator.comparing(Post::getCreatedAt))
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<LikeDTO> likes = user
                .getFollowing()
                .stream()
                .flatMap(following -> following.getLikes().stream())
                .sorted(Comparator.comparing(Like::getCreatedAt))
                .map(like -> modelMapper.map(like, LikeDTO.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<CommentDTO> comments = user
                .getFollowing()
                .stream()
                .flatMap(following -> following.getComments().stream())
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return NewsfeedDTO.builder()
                .posts(posts)
                .likes(likes)
                .comments(comments)
                .build();
    }
}
