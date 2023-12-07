package com.example.demo.repositories;

import com.example.demo.models.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing `Folder` entities in the database.
 * Extends `JpaRepository<Folder, UUID>` for basic CRUD operations.
 */
@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID> {

    /**
     * Retrieves a Folder if a folder with corresponding name is found.
     *
     * @param   name  The name of the sought after folder.
     * @return  An optional containing a folder, if one with the corresponding folder name is found.
     */
    Optional<Folder> findByName(String name);

    /**
     * Retrieves a Folder if a folder with corresponding id is found.
     *
     * @param id    The UUID of the sought after folder.
     * @return      An optional containing a folder, if one with the corresponding id is found.
     */
    Optional<Folder> findById(UUID id);

    /**
     * Retrieves a List of Folders that are owned by a specific account.
     *
     * @param ownerId    The UUID of a specific account whose folders are to be retrieved.
     * @return      A List of folders that has a specific ownerId.
     */
    List<Folder> findByOwnerId(UUID ownerId);

}
