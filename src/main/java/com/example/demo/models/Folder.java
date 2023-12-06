package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;
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
    private UUID ownerId;
    private Date created = new Date();

    public Folder(String name, UUID ownerId) {
        this.name = name;
        this.ownerId = ownerId;
    }



/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return Objects.equals(id, folder.id) &&
                Objects.equals(name, folder.name) &&
                Objects.equals(ownerId, folder.ownerId) &&
                Objects.equals(created, folder.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ownerId, created);
    }
    */
}