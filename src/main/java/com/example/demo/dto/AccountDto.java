package com.example.demo.dto;

import com.example.demo.models.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class AccountDto {
    private UUID id;
    private String username;
    private String password;
    private Date created;

    public AccountDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.created = account.getCreated();
    }
}




/*
@Getter
@Setter
public class StudentDto {

    private UUID id;
    private String name;
    private String email;

    public StudentDto(Student student) {
        this.name = student.getName();
        this.email = student.getEmail();
        this.id = student.getId();
    }

}
*/