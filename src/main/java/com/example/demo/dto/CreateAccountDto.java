package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class CreateAccountDto {
    private String username;
    private String password;



//Needed for the test mock data
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateAccountDto that = (CreateAccountDto) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

}
