package com.example.demo.security;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTVerifyFilter extends OncePerRequestFilter {

    private UserDetailsService userService;

    public JWTVerifyFilter(UserDetailsService userDetailsService) {
        this.userService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var algorithm = Algorithm.HMAC256("keyboardcatEV)+7yeVE)AV/Y345E)yvEA)7)(/&%vduHY7GEDW(/W¤Q#46578!)PWQ(FUIHdsU345v");
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
