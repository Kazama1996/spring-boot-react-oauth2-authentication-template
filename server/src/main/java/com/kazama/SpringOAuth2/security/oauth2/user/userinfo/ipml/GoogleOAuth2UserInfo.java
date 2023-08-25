package com.kazama.SpringOAuth2.security.oauth2.user.userinfo.ipml;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kazama.SpringOAuth2.security.AuthProvider;
import com.kazama.SpringOAuth2.security.oauth2.user.userinfo.OAuth2UserInfo;
import com.kazama.SpringOAuth2.security.oauth2.user.userinfo.UserInfofactory;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Autowired
    private UserInfofactory userInfofactory;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @PostConstruct
    private void init() {
        userInfofactory.register(AuthProvider.google.toString(), this);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
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
        return (String) attributes.get("picture");
    }

}
