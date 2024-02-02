package com.kostenko.demo.proxy.seller.controller;


import com.kostenko.demo.proxy.seller.dto.*;
import com.kostenko.demo.proxy.seller.entity.RefreshToken;
import com.kostenko.demo.proxy.seller.entity.User;
import com.kostenko.demo.proxy.seller.error.ApplicationError;
import com.kostenko.demo.proxy.seller.service.JwtService;
import com.kostenko.demo.proxy.seller.service.RefreshTokenService;
import com.kostenko.demo.proxy.seller.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling user authentication-related operations.
 */
@RestController
@RequestMapping("/user")
public class AuthController {

    /**
     * Length of life of a JWT access token in seconds.
     */
    @Value("${jwt.cookieExpiry}")
    private int cookieExpiry;

    /**
     * Service for handling refresh tokens.
     */
    private final RefreshTokenService refreshTokenService;

    /**
     * Service for handling JWT-related operations.
     */
    private final JwtService jwtService;

    /**
     * Authentication manager for user authentication.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Service for user-related operations.
     */
    private final UserService userService;

    /**
     * Constructs an AuthController with the specified dependencies.
     *
     * @param refreshTokenService   Service for handling refresh tokens.
     * @param jwtService            Service for handling JWT-related operations.
     * @param authenticationManager Authentication manager for user authentication.
     * @param userService           Service for user-related operations.
     */
    @Autowired
    public AuthController(RefreshTokenService refreshTokenService, JwtService jwtService,
                          AuthenticationManager authenticationManager, UserService userService) {
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }


    /**
     * Registers a new user based on the data provided in user registration DTO.
     *
     * @param userRequest   The user registration DTO containing user details.
     * @param bindingResult The result of the validation.
     *
     * @return A UserResponse representing the newly registered user.
     *
     * @throws IllegalArgumentException If the user registration data is invalid.
     */
    @PostMapping(value = "/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse saveUser(@RequestBody @Valid UserRegistrationDTO userRequest,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getAllErrors().toString());
        }

        return userService.saveNewUser(userRequest, null);
    }


    /**
     * Logs out the user by invalidating the cookie with access token.
     *
     * @param accessCookie The value of the access token cookie.
     * @param response     The HTTP servlet response.
     *
     * @return ResponseEntity with HTTP status 200.
     */
    @PostMapping(value = "/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> logOut(@CookieValue("accessToken") String accessCookie, HttpServletResponse response) {

        String accessToken = jwtService.GenerateToken(jwtService.extractUserId(accessCookie));

        ResponseCookie cookie = ResponseCookie
                .from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(null);
    }


    /**
     * Authenticates a user by validating the provided username and password.
     * If the authentication is successful, generates an access token and a refresh token,
     * associates the access token with a cookie, saves generated token
     * and returns a {@link JwtResponseDTO} containing the access and refresh tokens.
     *
     * @param authRequestDTO - Object with username and password for authentication
     * @param response       - Object of {@link HttpServletResponse} class, used for further use of cookies
     * @return JwtResponseDTO
     * - Object which should contain access and refresh tokens
     * @throws UsernameNotFoundException - If the provided user credentials are invalid.
     */
    @Operation(summary = "Login to the system using username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in successfully.",
                    content = @Content(schema = @Schema(implementation = JwtResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid user credentials.",
                    content = @Content(schema = @Schema(implementation = ApplicationError.class)))
    })
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));

        if (authentication.isAuthenticated()) {

            User user = userService.findByUsername(authRequestDTO.getUsername());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
            String accessToken = jwtService.GenerateToken(user.getId());
            // set accessToken to cookie header
            ResponseCookie cookie = ResponseCookie
                    .from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(cookieExpiry)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .token(refreshToken.getToken()).build();

        } else {
            throw new UsernameNotFoundException("Invalid user credentials.");
        }

    }


    /**
     * Refreshes a JWT token using a valid refresh token. The provided refresh token is validated,
     * and if it is valid, a new access token is generated for the associated user.
     *
     * @param refreshTokenRequestDTO - Object containing the refresh token to be used for token refresh.
     * @return JwtResponseDTO
     * - Object containing the refreshed access token along with the original refresh token.
     * @throws UsernameNotFoundException - If the provided refresh token is invalid.
     */
    @Operation(summary = "Refresh JWT token using refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully.",
                    content = @Content(schema = @Schema(implementation = JwtResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token is invalid.",
                    content = @Content(schema = @Schema(implementation = ApplicationError.class)))
    })
    @PostMapping("/refreshToken")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("Refresh token is invalid"));
    }

}