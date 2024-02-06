package com.kostenko.demo.proxy.seller.controller;

import com.kostenko.demo.proxy.seller.dto.UserEditDTO;
import com.kostenko.demo.proxy.seller.dto.UserPageDTO;
import com.kostenko.demo.proxy.seller.service.JwtService;
import com.kostenko.demo.proxy.seller.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling user-related operations.
 */
@RestController
@RequestMapping("/user")
public class UserController {


    /**
     * Constant error message template for error, if user want to subscribe to himself.
     */
    protected static final String SAME_ID_MESSAGE = "User requested and user mentioned is the same.";
    /**
     * Service for handling user-related operations.
     */
    private final UserService userService;
    /**
     * Service for handling JWT-related operations.
     */
    private final JwtService jwtService;

    /**
     * Constructs a UserController with the specified dependencies.
     *
     * @param userService Service for handling user-related operations.
     * @param jwtService  Service for handling JWT-related operations.
     */
    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }


    /**
     * Retrieves and returns the user page based on the provided user ID.
     *
     * @param userId The ID of the user for whom the page is requested.
     * @return A UserPageDTO representing the user page.
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/page/{userId}")
    UserPageDTO viewUserPage(@PathVariable(name = "userId") String userId) {
        return userService.getUserPage(userId);
    }


    /**
     * Deletes a user from the database based on the provided user ID.
     *
     * @param userId The ID of the user to be deleted.
     * @return ResponseEntity with HTTP status OK.
     */
    @Operation(summary = "Delete user from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted from database"),
            @ApiResponse(responseCode = "403", description = "Permission denied, user with id requested for deletion isn't the one who made request")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete/{userId}")
    ResponseEntity<HttpStatus> deleteUser(@PathVariable(name = "userId") String userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/edit/{userId}")
    ResponseEntity<?> editUser(@PathVariable(name = "userId") String userId,
                               @RequestBody @Valid UserEditDTO userRequest,
                               BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getAllErrors().toString());
        }
        if (userRequest.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(userService.edit(userRequest, userId), HttpStatus.OK);
    }


    /**
     * Follow to a user.
     *
     * @param userId       The ID of the user to follow.
     * @param accessCookie Cookie used to extract request sender ID.
     * @return ResponseEntity with HTTP status OK.
     */
    @Operation(summary = "Follow to a user")
    @ApiResponse(responseCode = "200", description = "User successfully following to a user requested")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/follow/{userId}")
    ResponseEntity<HttpStatus> followToUser(@PathVariable(name = "userId") String userId,
                                            @CookieValue("accessToken") String accessCookie
    ) {
        String requesterId = jwtService.extractUserId(accessCookie);

        if (requesterId.equals(userId)) {
            throw new IllegalArgumentException(SAME_ID_MESSAGE);
        }

        userService.followToUser(requesterId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Unfollow from user.
     *
     * @param userId       The ID of the user to unfollow.
     * @param accessCookie Cookie used to extract request sender ID.
     * @return ResponseEntity with HTTP status OK.
     */
    @Operation(summary = "Unfollow from user")
    @ApiResponse(responseCode = "200", description = "User successfully unfollowed from a user requested")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/unfollow/{userId}")
    ResponseEntity<HttpStatus> unfollowFromUser(@PathVariable(name = "userId") String userId,
                                                @CookieValue("accessToken") String accessCookie
    ) {
        String requesterId = jwtService.extractUserId(accessCookie);

        if (requesterId.equals(userId)) {
            throw new IllegalArgumentException(SAME_ID_MESSAGE);
        }

        userService.unfollowFromUser(requesterId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


// TODO rewrite Api docs:

// TODO add validators to both DTO's and entities