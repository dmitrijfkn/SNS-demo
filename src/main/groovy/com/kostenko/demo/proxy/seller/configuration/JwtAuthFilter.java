package com.kostenko.demo.proxy.seller.configuration;

import com.kostenko.demo.proxy.seller.entity.User;
import com.kostenko.demo.proxy.seller.service.JwtService;
import com.kostenko.demo.proxy.seller.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for handling JWT-based authentication. Extracts JWT token from the request,
 * validates it, and sets the authenticated user in the security context.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * Service for handling JWT-related operations.
     */
    private final JwtService jwtService;

    /**
     * Service for loading user details based on user identifiers.
     */
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     * Constructs a JwtAuthFilter with the specified JWT service and user details service.
     *
     * @param jwtService             Service for handling JWT-related operations.
     * @param userDetailsServiceImpl Service for loading user details based on user identifiers.
     */
    @Autowired
    public JwtAuthFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    /**
     * Filters incoming requests, extracts and validates JWT token, and sets the authenticated user in the security context.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     *
     * @throws ServletException If an error occurs while processing the request.
     * @throws IOException      If an error occurs with I/O operations.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        String userId = null;

        // Extract the JWT token from the "accessToken" cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("accessToken")) {
                    token = cookie.getValue();
                }
            }
        }

        // If no token is found, continue with the filter chain
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the user ID from the JWT token
        userId = jwtService.extractUserId(token);

        // If user ID is found, load user details and validate the token
        if (userId != null) {
            User user = userDetailsServiceImpl.loadUserByUserId(userId);
            if (jwtService.validateToken(token, user)) {
                // If the token is valid, set the authentication token in the security context
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
