package com.kazama.SpringOAuth2.security.oauth2.user.userinfo;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class UserInfofactory {

    private static Map<String, OAuth2UserInfo> infos = new ConcurrentHashMap<>();

    public static OAuth2UserInfo getByInfoType(String type, Map<String, Object> attributes) {
        System.out.println("--------------------------------------------------");
        System.out.println("Size of the map is :" + infos.size() + "Type is : " + type);
        System.out.println("--------------------------------------------------");
        OAuth2UserInfo userInfoClass = infos.get(type);
        try {
            Constructor<? extends OAuth2UserInfo> constructor = userInfoClass.getClass().getConstructor(Map.class);
            return constructor.newInstance(attributes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void register(String infoType, OAuth2UserInfo oAuth2UserInfo) {
        Assert.notNull(infoType, "infoType can not be null");
        System.out.println("--------------------------------------------------");
        System.out.println("Register success you regist the infoType : " + infoType);
        System.out.println("--------------------------------------------------");
        infos.put(infoType, oAuth2UserInfo);
    }
}
