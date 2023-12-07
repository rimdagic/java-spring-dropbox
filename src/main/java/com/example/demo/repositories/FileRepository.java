package com.example.demo.repositories;

import com.example.demo.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing `File` entities in the database.
 * Extends `JpaRepository<File, UUID>` for basic CRUD operations.
 */
@Repository
public interface FileRepository extends JpaRepository<File, UUID> {

    /**
     * This method retrieves a list of files from the database based on the folder that the file belongs to.
     *
     * @param folderId  The UUID of the specific folder whose content will be retrieved
     * @return          A list of the files in the specified folder.
     */
    @Query(value = "SELECT * FROM file WHERE folder_id = :folderId", nativeQuery = true)
    List<File> findFilesByFolderId(@Param("folderId") UUID folderId);

}
