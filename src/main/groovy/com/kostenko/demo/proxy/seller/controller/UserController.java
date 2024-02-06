package com.kostenko.demo.proxy.seller.controller;

import com.kostenko.demo.proxy.seller.dto.ApplicationErrorDTO;
import com.kostenko.demo.proxy.seller.dto.UserEditDTO;
import com.kostenko.demo.proxy.seller.dto.UserPageDTO;
import com.kostenko.demo.proxy.seller.dto.UserResponse;
import com.kostenko.demo.proxy.seller.error.ResourceNotFoundException;
import com.kostenko.demo.proxy.seller.service.JwtService;
import com.kostenko.demo.proxy.seller.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    @Operation(summary = "See user page of user with certain id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User with requested id founded and returned.",
                    content = @Content(schema = @Schema(implementation = UserPageDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "User with requests id doesn't present in database.",
                    content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class)))
    })
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
            @ApiResponse(responseCode = "200",
                    description = "User successfully deleted from database"),
            @ApiResponse(responseCode = "403",
                    description = "Permission denied, user with id requested for deletion isn't the one who made request")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete/{userId}")
    ResponseEntity<HttpStatus> deleteUser(@PathVariable(name = "userId") String userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Deletes a user from the database based on the provided user ID.
     *
     * @param userId        The ID of the user to be edited.
     * @param userRequest   DTO object with data to change.
     * @param bindingResult Object used to validate input data.
     * @return ResponseEntity with HTTP status OK and edited user data
     * @throws IllegalArgumentException  if user with provided username already exists or data provided is invalid
     * @throws ResourceNotFoundException if user with provided userId doesn't exist
     */
    @Operation(summary = "Edit user data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User successfully edited",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Request data have errors",
                    content = @Content(schema = @Schema(implementation = ObjectError.class))),
            @ApiResponse(responseCode = "403",
                    description = "Permission denied, user with id requested for edit isn't the one who made request")
    })
    @ResponseStatus(HttpStatus.OK)
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
     * Follow a user.
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