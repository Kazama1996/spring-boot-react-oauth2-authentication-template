package com.kazama.SpringOAuth2.security.oauth2;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.kazama.SpringOAuth2.model.User;

public class UserPrincipal implements OAuth2User, UserDetails {

    private UUID userId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(UUID userId, String email, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {

        List<GrantedAuthority> authorities = Collections
                .singletonList(new SimpleGrantedAuthority(user.getRole().toString()));

        return new UserPrincipal(user.getUserId(), user.getEmail(), user.getPassword(), authorities);
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return this.attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return authorities;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return this.userId.toString();
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return this.password;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

}
