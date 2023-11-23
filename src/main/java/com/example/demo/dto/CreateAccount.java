package com.example.demo.dto;

import lombok.Getter;

@Getter
public class CreateAccount {
    private String username;
    private String password;

    public CreateAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }


}
