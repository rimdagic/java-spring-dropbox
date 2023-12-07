package com.example.demo.repositories;

import com.example.demo.models.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing `Account` entities in the database.
 * Extends `JpaRepository<Account, UUID>` for basic CRUD operations.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    /**
     * Retrieves an `Optional<Account>` by the specified username.
     *
     * @param username  The username of the sought after Account
     * @return          Optional that either contains an Account or not.
     */
    Optional<Account> findByUsername(String username);

    /**
     * Deletes an account from the database based on the specified username.
     * Uses a JPQL query to delete the specified account.
     *
     * @param username
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Account a WHERE a.username = :username")
    void deleteByUsername(String username);

}
