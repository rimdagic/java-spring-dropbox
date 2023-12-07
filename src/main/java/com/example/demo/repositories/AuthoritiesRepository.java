package com.example.demo.repositories;

import com.example.demo.models.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing `Authorities` entities in the database.
 * Extends `JpaRepository<Authorities, UUID>` for basic CRUD operations.
 */
@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, UUID> {

}
