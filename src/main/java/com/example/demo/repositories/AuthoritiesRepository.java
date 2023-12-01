package com.example.demo.repositories;

import com.example.demo.models.Account;
import com.example.demo.models.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, UUID> {


}
