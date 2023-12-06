package com.example.demo.repositories;

import com.example.demo.models.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM Account a WHERE a.username = :username")
    void deleteByUsername(String username);

}
