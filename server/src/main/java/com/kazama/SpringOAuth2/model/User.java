// package com.kazama.jwt.model;

// import java.time.ZonedDateTime;
// import java.util.Collection;
// import java.util.List;
// import java.util.UUID;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;

// import com.kazama.jwt.security.AuthProvider;
// import com.kazama.jwt.security.Role;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @AllArgsConstructor
// @NoArgsConstructor
// @Data
// @Entity
// @Table(name = "app_user")
// @Builder
// public class User implements UserDetails {

//     @Id
//     @GeneratedValue(strategy = GenerationType.UUID)
//     @Column(name = "user_id")
//     private UUID userId;

//     @Column(name = "email", length = 100)
//     private String email;

//     @Column(name = "password", length = 250)
//     private String password;

//     @Column(name = "profile_name", length = 50)
//     private String profileName;

//     @Column(name = "full_name", length = 100)
//     private String fullName;

//     private ZonedDateTime updateAt;

//     @Enumerated(EnumType.STRING)
//     @Column(name = "role")
//     private Role role;

//     @Enumerated(EnumType.STRING)
//     @Column(name = "provider")
//     private AuthProvider authProvider;

//     @Override
//     public Collection<? extends GrantedAuthority> getAuthorities() {
//         return List.of(new SimpleGrantedAuthority(this.role.name()));
//     }

//     @Override
//     public String getUsername() {
//         // TODO Auto-generated method stub
//         return this.getEmail();
//     }

//     @Override
//     public boolean isAccountNonExpired() {
//         // TODO Auto-generated method stub
//         return true;
//     }

//     @Override
//     public boolean isAccountNonLocked() {
//         // TODO Auto-generated method stub
//         return true;
//     }

//     @Override
//     public boolean isCredentialsNonExpired() {
//         // TODO Auto-generated method stub
//         return true;
//     }

//     @Override
//     public boolean isEnabled() {
//         // TODO Auto-generated method stub
//         return true;
//     }

// }

package com.kazama.SpringOAuth2.model;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
