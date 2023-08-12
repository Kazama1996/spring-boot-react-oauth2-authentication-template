package com.kazama.jwt.config.security.oauth2.user;

import java.nio.file.attribute.UserPrincipal;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kazama.jwt.config.security.AuthProvider;
import com.kazama.jwt.config.security.oauth2.user.userinfo.OAuth2UserInfo;
import com.kazama.jwt.config.security.oauth2.user.userinfo.UserInfofactory;
import com.kazama.jwt.dao.UserRepository;
import com.kazama.jwt.exception.security.OAuth2AuthenticationProcessingException;
import com.kazama.jwt.model.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private UserRepository userRepository;

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {

        User user = User.builder()
                .authProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .build();

        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFullName(oAuth2UserInfo.getName());
        existingUser.setProfileName(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);

    }

    private OAuth2User processOauth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User)
            throws OAuth2AuthenticationProcessingException {
        String infoType = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = UserInfofactory
                .getByInfoType(infoType);
        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from your provider");
        }

        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElse(registerNewUser(oAuth2UserRequest, oAuth2UserInfo));

        if (user.getAuthProvider()
                .equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
            throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                    user.getAuthProvider() + " account. Please use your " + user.getAuthProvider() +
                    " account to login.");
        }

        updateExistingUser(user, oAuth2UserInfo);
        return null;

    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        return null;
    }

}
