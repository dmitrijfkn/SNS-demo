package com.kostenko.demo.proxy.seller.controller;

import com.kostenko.demo.proxy.seller.dto.NewsfeedDTO;
import com.kostenko.demo.proxy.seller.dto.PostCreationDTO;
import com.kostenko.demo.proxy.seller.dto.PostDTO;
import com.kostenko.demo.proxy.seller.service.JwtService;
import com.kostenko.demo.proxy.seller.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling post-related operations.
 */
@RestController
@RequestMapping("/post")
public class PostController {

    /**
     * Service for handling post-related operations.
     */
    private final PostService postService;

    /**
     * Service for handling JWT-related operations.
     */
    private final JwtService jwtService;


    /**
     * Constructs a PostController with the specified dependencies.
     *
     * @param postService Service for handling post-related operations.
     * @param jwtService  Service for handling JWT-related operations.
     */
    @Autowired
    public PostController(PostService postService, JwtService jwtService) {
        this.postService = postService;
        this.jwtService = jwtService;
    }


    /**
     * Creates a new post based on the provided content and access token cookie.
     *
     * @param postCreationDTO Dto object which should contain the content of the post.
     * @param accessCookie    The value of the access token cookie.
     * @return A PostDTO representing the newly created post.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    PostDTO createPost(@RequestBody PostCreationDTO postCreationDTO,
                       @CookieValue("accessToken") String accessCookie) {
        String userId = jwtService.extractUserId(accessCookie);

        return postService.createPost(userId, postCreationDTO.getContent());
    }


    /**
     * Deletes a post based on the provided post ID and user ID from access token cookie.
     * If post with requested id doesn't exist, silently return OK
     *
     * @param postId       The ID of the post to be deleted.
     * @param accessCookie The value of the access token cookie.
     * @return ResponseEntity with HTTP status OK.
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/edit/{postId}")
    ResponseEntity<?> deletePost(@PathVariable(name = "postId") String postId,
                                          @RequestBody PostCreationDTO postDTO,
                                          @CookieValue("accessToken") String accessCookie) {
        String userId = jwtService.extractUserId(accessCookie);

        return new ResponseEntity<>(postService.editPost(postId, postDTO,userId),HttpStatus.OK);
    }


    /**
     * Deletes a post based on the provided post ID and user ID from access token cookie.
     * If post with requested id doesn't exist, silently return OK
     *
     * @param postId       The ID of the post to be deleted.
     * @param accessCookie The value of the access token cookie.
     * @return ResponseEntity with HTTP status OK.
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete/{postId}")
    ResponseEntity<HttpStatus> deletePost(@PathVariable(name = "postId") String postId,
                                          @CookieValue("accessToken") String accessCookie) {
        String userId = jwtService.extractUserId(accessCookie);

        postService.deletePost(postId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Saves a post as favorite based on the provided post ID and user ID from access token cookie.
     *
     * @param postId       The ID of the post to be saved to favorites.
     * @param accessCookie The value of the access token cookie.
     * @return ResponseEntity with HTTP status OK.
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/favorite/add/{postId}")
    ResponseEntity<HttpStatus> addPostToFavorites(@PathVariable(name = "postId") String postId,
                                                  @CookieValue("accessToken") String accessCookie) {
        String userId = jwtService.extractUserId(accessCookie);

        postService.addPostToFavorites(userId, postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Deletes a post from favorite based on the provided post ID and user ID from access token cookie.
     *
     * @param postId       The ID of the post to be saved to favorites.
     * @param accessCookie The value of the access token cookie.
     * @return ResponseEntity with HTTP status OK.
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/favorite/remove/{postId}")
    ResponseEntity<HttpStatus> removePostFromFavorites(@PathVariable(name = "postId") String postId,
                                                       @CookieValue("accessToken") String accessCookie) {
        String userId = jwtService.extractUserId(accessCookie);

        postService.removePostFromFavorites(userId, postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Adds like to a post based on the provided post ID and user ID from access token cookie.
     *
     * @param postId       The ID of the post.
     * @param accessCookie The value of the access token cookie.
     * @return ResponseEntity with HTTP status OK.
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/like/add/{postId}")
    ResponseEntity<HttpStatus> addLikeToPost(@PathVariable(name = "postId") String postId,
                                             @CookieValue("accessToken") String accessCookie) {
        String userId = jwtService.extractUserId(accessCookie);

        postService.addLikeToPost(userId, postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Remove like to a post based on the provided post ID and user ID from access token cookie.
     *
     * @param postId       The ID of the post.
     * @param accessCookie The value of the access token cookie.
     * @return ResponseEntity with HTTP status OK.
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/like/remove/{postId}")
    ResponseEntity<HttpStatus> removeLikeFromPost(@PathVariable(name = "postId") String postId,
                                                  @CookieValue("accessToken") String accessCookie) {
        String userId = jwtService.extractUserId(accessCookie);

        postService.removeLikeFromPost(userId, postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/newsfeed/{userId}")
    NewsfeedDTO newsfeed(@PathVariable(name = "userId") String userId) {
        return postService.newsfeed(userId);
    }
}