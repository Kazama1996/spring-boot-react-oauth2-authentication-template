package com.kazama.jwt.config.security.oauth2.user.userinfo.ipml;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.kazama.jwt.config.security.oauth2.user.userinfo.OAuth2UserInfo;
import com.kazama.jwt.config.security.oauth2.user.userinfo.UserInfofactory;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo, InitializingBean {

    private Map<String, Object> attributes;

    private UserInfofactory userInfofactory;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
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

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        userInfofactory.register("GOOGLE", this);
    }

}
