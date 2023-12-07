package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *  A filter responsible for verifying and processing JSON Web Tokens in incoming HTTP requests.
 *  The class extends `OncePerRequestFilter` to ensure that the filter only is applied once per request.
 *  This filter is used to authenticate users and validate JWTs in the application.
 */
public class JWTVerifyFilter extends OncePerRequestFilter {

    private UserDetailsService userService;

    /**
     * A constructor that initializes an instance of the `UserDetailsService` which can retrieve user accounts based
     * on a username. So it can be used within the `JWTVerifyFilter` class.
     *
     * @param userDetailsService An instance of the `UserDetailsService` class.
     */
    public JWTVerifyFilter(UserDetailsService userDetailsService) {
        this.userService = userDetailsService;
    }

    /**
     * The `doFilterInternal` method handles the authentication header in the security filter chain and authenticates
     * valid JWT. Then lets the request continue through the filter chain either authenticated or unauthenticated.
     *
     * @param request           The incoming HTTP request.
     * @param response          The generated HTTP response.
     * @param filterChain       The filter chain for processing subsequent filters.
     * @throws ServletException If an error occurs during the authentication process.
     * @throws IOException      If an I/O error occurs during filtering.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var algorithm = Algorithm.HMAC256("keyboardcat");
            var verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

            var jwt = verifier.verify(authHeader);
            var account = this.userService.loadUserByUsername(jwt.getSubject());

            var auth = new UsernamePasswordAuthenticationToken(account, account.getPassword(), account.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);

        } catch (JWTVerificationException exception) {
            throw new IllegalStateException("Failed to authenticate");
        }
    }

}
