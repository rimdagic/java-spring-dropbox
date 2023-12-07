package com.example.demo.models;

import com.example.demo.dto.CreateAccountDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * The `Account` class represents a user account. Instances of the `Account` are used to store and retrieve user
 * accounts to the account repository and the database.
 * The `Account` class holds five different attributes.
 * <p>
 * -id, the unique id for the account in the database table.
 * -username, a string containing the username, which also is forced to be unique.
 * -password, stores a hashed version of the account's password. This column is ignored when represented in JSON to
 *      avoid the password from being returned to the front-end.
 * -created, A Date object containing the point in time when the account was first created.
 * -authorities, a collection of authorities that the user have, authorities are stored in another table hence the
 *      ManyToOne annotation
 * <p>
 * The class is annotated with Entity to ensure it can represent a repository and database entity. All attributes have
 * getters and setters and a constructor without arguments. There is also a constructur that initializes an instance of
 * the class by taking an instance of an CreateAccountDto to save  to the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;
    private Date created = new Date();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Collection<Authorities> authorities;

    public Account(CreateAccountDto createAccountDto) {
        this.username = createAccountDto.getUsername();
        this.password = createAccountDto.getPassword();
    }
}
