package com.kostenko.demo.proxy.seller.controller;

import com.kostenko.demo.proxy.seller.dto.ApplicationErrorDTO;
import com.kostenko.demo.proxy.seller.dto.NewsfeedDTO;
import com.kostenko.demo.proxy.seller.dto.PostCreationDTO;
import com.kostenko.demo.proxy.seller.dto.PostDTO;
import com.kostenko.demo.proxy.seller.error.ResourceNotFoundException;
import com.kostenko.demo.proxy.seller.service.JwtService;
import com.kostenko.demo.proxy.seller.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create a new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Post created successfully.",
                    content = @Content(schema = @Schema(implementation = PostDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Provided data is incorrect, post can't be created.",
                    content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "User which made request can't be founded.",
                    content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class)))
    })
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
     * @param postDTO      Data for post update
     * @param accessCookie The value of the access token cookie.
     * @return ResponseEntity with HTTP status OK.
     */
    @Operation(summary = "Edit a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Post edited successfully."),
            @ApiResponse(responseCode = "403",
                    description = "User which made request isn't a author of a post.",
                    content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Post with specified id don't exist.",
                    content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class)))
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/edit/{postId}")
    ResponseEntity<?> editPost(@PathVariable(name = "postId") String postId,
                               @RequestBody PostCreationDTO postDTO,
                               @CookieValue("accessToken") String accessCookie) {
        String userId = jwtService.extractUserId(accessCookie);

        return new ResponseEntity<>(postService.editPost(postId, postDTO, userId), HttpStatus.OK);
    }


    /**
     * Deletes a post based on the provided post ID and user ID from access token cookie.
     * If post with requested id doesn't exist, silently return OK
     *
     * @param postId       The ID of the post to be deleted.
     * @param accessCookie The value of the access token cookie.
     * @return ResponseEntity with HTTP status OK.
     */
    @Operation(summary = "Deletes a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Post deleted successfully."),
            @ApiResponse(responseCode = "403",
                    description = "User which made request isn't a author of a post.",
                    content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Post with specified id don't exist.",
                    content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class)))
    })
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
    @Operation(summary = "Add post to favorite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Post added to favorites successfully."),
            @ApiResponse(responseCode = "404",
                    description = "Post with specified id don't exist.",
                    content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class)))
    })
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
    @Operation(summary = "Add post to favorite")
    @ApiResponse(responseCode = "200",
            description = "Post deleted from favorites successfully.")
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
    @Operation(summary = "Add like to a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Post liked successfully."),
            @ApiResponse(responseCode = "404",
                    description = "Post or user which made request don't exist.",
                    content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class)))
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/like/add/{postId}")
    ResponseEntity<HttpStatus> addLikeToPost(@PathVariable(name = "postId") String postId,
                                             @CookieValue("accessToken") String accessCookie) {
        String userId = jwtService.extractUserId(accessCookie);

        postService.addLikeToPost(userId, postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Remove like from a post based on the provided post ID and user ID from access token cookie.
     *
     * @param postId       The ID of the post.
     * @param accessCookie The value of the access token cookie.
     * @return ResponseEntity with HTTP status OK.
     */
    @Operation(summary = "Removes like from a post")
    @ApiResponse(responseCode = "200",
            description = "Like removed successfully.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/like/remove/{postId}")
    ResponseEntity<HttpStatus> removeLikeFromPost(@PathVariable(name = "postId") String postId,
                                                  @CookieValue("accessToken") String accessCookie) {
        String userId = jwtService.extractUserId(accessCookie);

        postService.removeLikeFromPost(userId, postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Retrieves the newsfeed for a specified user.
     *
     * @param userId The unique identifier of the user whose newsfeed is to be retrieved.
     * @return The newsfeed of the specified user.
     * @throws ResourceNotFoundException if user with specified id doesn't exist
     */
    @Operation(summary = "Get user newsfeed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User newsfeed returned successfully.",
                    content = @Content(schema = @Schema(implementation = NewsfeedDTO.class))),
            @ApiResponse(responseCode = "403",
                    description = "User who made request didn't the one which newsfeed need to be returned.",
                    content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "User with id provided don't exist.",
                    content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class)))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/newsfeed/{userId}")
    NewsfeedDTO newsfeed(@PathVariable(name = "userId") String userId) {
        return postService.newsfeed(userId);
    }
}