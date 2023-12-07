package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;

/**
 * The `Authorities` class represents an authority type.
 * The class holds two different attributes.
 * <p>
 * -id, that in this case is of the Long data type to identify a specific authority.
 * -authorityName, a string containing the name of an authority, such as "ADMIN" or "USER".
 * <p>
 * Instances of the class are used to grant specific users authorities, such as deleting accounts and retrieving a
 * list of all the users in the database.
 */
@Entity
public class Authorities implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String authorityName;

    /**
     * Returns the authority name of the Authority instance object.
     * This method is overridden since it is part of the `GrantedAuthority` interface.
     * It is used to provide the authority for use in authentication and authorization.
     *
     * @return The name of the authority.
     */
    @Override
    public String getAuthority() {
        return authorityName;
    }
}