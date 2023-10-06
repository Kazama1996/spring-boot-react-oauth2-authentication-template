package com.kazama.SpringOAuth2.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.kazama.SpringOAuth2.security.AuthProvider;
import com.kazama.SpringOAuth2.security.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "app_user")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 250)
    private String password;

    @Column(name = "profile_name", length = 50)
    private String profileName;

    @Column(name = "full_name", length = 100)
    private String fullName;

    private ZonedDateTime updateAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private AuthProvider authProvider;

    @Column(name = "provider_id")
    private String providerId;

}
