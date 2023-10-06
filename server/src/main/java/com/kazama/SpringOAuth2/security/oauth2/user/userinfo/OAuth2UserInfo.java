package com.kazama.SpringOAuth2.security.oauth2.user.userinfo;

public interface OAuth2UserInfo {

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

}
