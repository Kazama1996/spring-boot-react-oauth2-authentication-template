package com.kazama.jwt.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazama.jwt.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByprofileName(String profileName);

    boolean existsByemail(String email);

    boolean existsByProfileName(String profileName);
}
