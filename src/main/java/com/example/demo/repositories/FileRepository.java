package com.example.demo.repositories;

import com.example.demo.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {

    File findByName(String name);


    @Query(value = "SELECT * FROM file WHERE folder_id = :folderId", nativeQuery = true)
    List<File> findFilesByFolderId(@Param("folderId") UUID folderId);

}
