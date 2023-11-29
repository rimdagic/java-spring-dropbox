package com.example.demo.models;

import com.example.demo.dto.CreateAccountDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

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
    private String password;
    private Date created = new Date();

    public Account(CreateAccountDto createAccountDto) {
        this.username = createAccountDto.getUsername();
        this.password = createAccountDto.getPassword();
    }
}
