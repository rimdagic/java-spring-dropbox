package com.example.demo.security;

import com.example.demo.repositories.AccountRepository;
import com.example.demo.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for security settings in the application.
 * Enables and configures Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * The `SecurityFilterChain` class filters requests matches requests to their level of authentication to ensure
     * application security.
     *
     * @param security           The `HttpSecurity` configuration object.
     * @param userDetailsService The `UserDetailsService` used for authentication.
     * @return The configured `SecurityFilterChain`.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, UserDetailsService userDetailsService)
            throws Exception {
        security
                .csrf(csrf -> csrf.disable())
                .addFilterAfter(new JWTVerifyFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/account/register").permitAll()
                        .requestMatchers("/account/login").permitAll()

                        .requestMatchers("/account/all").hasAuthority("ADMIN")
                        .requestMatchers("/account/{username}").hasAuthority("ADMIN")

                        .anyRequest().authenticated());

        return security.build();
    }

    /**
     * Creates and configures an `AuthenticationProvider` for user authentication.
     *
     * @param userService The `UserDetailsService` used for retrieving user details.
     * @param encoder     The `PasswordEncoder` used for password encoding.
     * @return An `AuthenticationProvider` configured for the application.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userService,
            PasswordEncoder encoder) {
        var dao = new DaoAuthenticationProvider();

        dao.setUserDetailsService(userService);
        dao.setPasswordEncoder(encoder);

        return dao;
    }

    /**
     * Creates and configures a `UserDetailsService` for retrieving user details during authentication.
     *
     * @param passwordEncoder   The `PasswordEncoder` used for hashing password.
     * @param accountRepository The `AccountRepository` for accessing user account information.
     * @return A `UserDetailsService` configured for the application.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        return new UserService(accountRepository, passwordEncoder);
    }

    /**
     * Creates and returns a `PasswordEncoder` for encoding and verifying passwords.
     *
     * @return A `PasswordEncoder` configured for the application.
     */
    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
