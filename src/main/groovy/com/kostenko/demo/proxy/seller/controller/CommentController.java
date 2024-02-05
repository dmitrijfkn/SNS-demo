package com.kostenko.demo.proxy.seller.controller;

import com.kostenko.demo.proxy.seller.dto.CommentCreationDTO;
import com.kostenko.demo.proxy.seller.dto.CommentDTO;
import com.kostenko.demo.proxy.seller.service.CommentService;
import com.kostenko.demo.proxy.seller.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Controller class for handling comment-related operations.
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    /**
     * Service for handling JWT-related operations.
     */
    private final JwtService jwtService;

    /**
     * Service for handling comment-related operations.
     */
    private final CommentService commentService;


    /**
     * Constructs a CommentController with the specified dependencies.
     *
     * @param jwtService     Service for handling JWT-related operations.
     * @param commentService Service for handling comment-related operations.
     */
    @Autowired
    public CommentController(JwtService jwtService,
                             CommentService commentService) {
        this.jwtService = jwtService;
        this.commentService = commentService;
    }


    /**
     * Creates a new comment on a post based on the provided content and access token cookie.
     *
     * @param commentCreationDTO DTO object which should contain comment and id of a post which is commented.
     * @param accessCookie       The value of the access token cookie.
     * @return A {@link com.kostenko.demo.proxy.seller.dto.CommentDTO} representing the newly created comment.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    CommentDTO createComment(@RequestBody @Valid CommentCreationDTO commentCreationDTO,
                             @CookieValue("accessToken") String accessCookie,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getAllErrors().toString());
        }

        String userId = jwtService.extractUserId(accessCookie);

        return commentService.createComment(userId, commentCreationDTO.getContent(), commentCreationDTO.getPostId());
    }


    /**
     * Returns all comments under post with ID requested
     *
     * @param postId id of a post to find comments of
     * @return A {@link java.util.Set} of {@link com.kostenko.demo.proxy.seller.dto.CommentDTO}'s.
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/postComments/{postId}")
    Set<CommentDTO> getPostComments(@PathVariable(name = "postId") String postId) {
        return commentService.getPostComments(postId);
    }
}