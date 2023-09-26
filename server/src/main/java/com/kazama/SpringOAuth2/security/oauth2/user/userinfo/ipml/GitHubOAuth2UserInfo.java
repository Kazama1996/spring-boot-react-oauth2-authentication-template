package com.kazama.SpringOAuth2.security.oauth2.user.userinfo.ipml;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kazama.SpringOAuth2.security.AuthProvider;
import com.kazama.SpringOAuth2.security.oauth2.user.userinfo.OAuth2UserInfo;
import com.kazama.SpringOAuth2.security.oauth2.user.userinfo.UserInfofactory;

import jakarta.annotation.PostConstruct;

@Component
public class GitHubOAuth2UserInfo implements OAuth2UserInfo {

    @Autowired
    private UserInfofactory userInfofactory;

    private Map<String, Object> attributes;

    @PostConstruct
    private void init() {
        userInfofactory.register(AuthProvider.github.toString(), this);
    }

    public GitHubOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");

    }

}
