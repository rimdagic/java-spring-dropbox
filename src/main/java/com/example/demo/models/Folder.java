package com.example.demo.models;

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
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;

    @Column(name = "account_id")
    private UUID accountId;
    private Date created = new Date();

    public Folder(String name, UUID accountId) {
        this.name = name;
        this.accountId = accountId;
    }
}