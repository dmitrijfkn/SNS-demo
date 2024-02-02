package com.kostenko.demo.proxy.seller.controller;

import com.kostenko.demo.proxy.seller.dto.CommentCreationDTO;
import com.kostenko.demo.proxy.seller.dto.CommentDTO;
import com.kostenko.demo.proxy.seller.service.JwtService;
import com.kostenko.demo.proxy.seller.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
     * Service for handling post-related operations.
     */
    private final PostService postService;

    /**
     * Constructs a CommentController with the specified dependencies.
     *
     * @param jwtService  Service for handling JWT-related operations.
     * @param postService Service for handling post-related operations.
     */
    @Autowired
    public CommentController(JwtService jwtService, PostService postService) {
        this.jwtService = jwtService;
        this.postService = postService;
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

        return postService.createComment(userId, commentCreationDTO.getContent(), commentCreationDTO.getPostId());
    }
}