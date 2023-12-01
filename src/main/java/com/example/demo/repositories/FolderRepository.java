package com.example.demo.repositories;

import com.example.demo.models.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID> {

    Optional<Folder> findByName(String name);

    Optional<Folder> findById(UUID id);

}
