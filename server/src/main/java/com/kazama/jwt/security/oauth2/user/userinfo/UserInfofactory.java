package com.kazama.jwt.security.oauth2.user.userinfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;

public class UserInfofactory {

    private static Map<String, OAuth2UserInfo> infos = new ConcurrentHashMap<String, OAuth2UserInfo>();

    public static OAuth2UserInfo getByInfoType(String type) {

        return infos.get(type);
    }

    public void register(String infoType, OAuth2UserInfo oAuth2UserInfo) {
        Assert.notNull(infoType, "infoType can not be null");
        infos.put(infoType, oAuth2UserInfo);
    }
}
