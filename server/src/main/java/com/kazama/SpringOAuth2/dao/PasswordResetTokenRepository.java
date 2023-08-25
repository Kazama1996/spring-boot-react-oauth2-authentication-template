package com.kazama.SpringOAuth2.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazama.SpringOAuth2.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByEmail(String email);

    void deleteByEmail(String email);
}
