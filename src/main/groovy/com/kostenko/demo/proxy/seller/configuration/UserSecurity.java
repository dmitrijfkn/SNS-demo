package com.kostenko.demo.proxy.seller.configuration;


import com.kostenko.demo.proxy.seller.entity.User;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * Custom {@link org.springframework.security.authorization.AuthorizationManager} for handling user-related access restrictions based on user ID.
 */
@Component
public class UserSecurity implements AuthorizationManager<RequestAuthorizationContext> {

    /**
     * Checks whether the authenticated user has the specified user ID.
     *
     * @param authenticationSupplier The supplier for obtaining the Authentication object.
     * @param ctx                    The context containing variables from the request.
     * @return The AuthorizationDecision based on whether the authenticated user has the specified user ID.
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext ctx) {
        String userId = ctx.getVariables().get("userId");
        Authentication authentication = authenticationSupplier.get();

        return new AuthorizationDecision(hasUserId(authentication, userId));
    }

    /**
     * Checks whether the authenticated user has the specified user ID.
     *
     * @param authentication The Authentication object representing the authenticated user.
     * @param userId         The user ID to check against.
     * @return true if the authenticated user has the specified user ID, false otherwise.
     */
    public boolean hasUserId(Authentication authentication, String userId) {
        return !authentication.getPrincipal().equals("anonymousUser")
                && authentication.isAuthenticated()
                && ((User) authentication.getPrincipal()).getId().equals(userId);
    }
}