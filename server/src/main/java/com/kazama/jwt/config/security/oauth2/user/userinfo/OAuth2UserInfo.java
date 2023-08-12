package com.kazama.jwt.config.security.oauth2.user.userinfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface OAuth2UserInfo {

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

}
