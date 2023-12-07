package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * The `Folder` class represents the Folder entity that can be saved and retrieved from the database.
 * The class holds four different attributes and each instance of the class represents a folder in the database.
 * <p>
 * -id, of the UUID type is used to identify a specific folder since it is the only unique attribute.
 * -name, folder name that the user will use to identify among its own folders.
 * -ownerId, contains a UUID that represents the account that created the folder hence have access to the folder.
 * -created, stores a pont in time when the fodler was first created.
 * <p>
 */
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
}