package com.kostenko.demo.proxy.seller.controller;

import com.kostenko.demo.proxy.seller.dto.UserPageDTO;
import com.kostenko.demo.proxy.seller.service.JwtService;
import com.kostenko.demo.proxy.seller.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling user-related operations.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * Service for handling user-related operations.
     */
    private final UserService userService;


    /**
     * Constructs a UserController with the specified dependencies.
     *
     * @param userService Service for handling user-related operations.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves and returns the user page based on the provided user ID.
     *
     * @param userId The ID of the user for whom the page is requested.
     *
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
     *
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
}

// TODO remove all entities from request params https://stackoverflow.com/questions/69639251/should-entity-class-be-used-as-request-body

// TODO search for entities in response

// TODO add @ResponseStatus to requests

// TODO rewrite Api docs:

// TODO add validators to both DTO's and entities

// Api docs example:
/**
 * @Operation(summary = "Save new author")
 * @ApiResponses(value = {
 * @ApiResponse(responseCode = "201", description = "Author saved successfully",
 * content = {@Content(mediaType = "application/json",
 * schema = @Schema(implementation = Author.class))}),
 * @ApiResponse(responseCode = "400", description = "Invalid request",
 * content = {@Content(mediaType = "application/json",
 * schema = @Schema(implementation = ApplicationError.class))})
 * })
 */