package com.kazama.SpringOAuth2.security.oauth2;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kazama.SpringOAuth2.dao.UserRepository;
import com.kazama.SpringOAuth2.exception.security.OAuth2AuthenticationProcessingException;
import com.kazama.SpringOAuth2.model.User;
import com.kazama.SpringOAuth2.security.AuthProvider;
import com.kazama.SpringOAuth2.security.Role;
import com.kazama.SpringOAuth2.security.oauth2.user.userinfo.OAuth2UserInfo;
import com.kazama.SpringOAuth2.security.oauth2.user.userinfo.UserInfofactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("******************************************************");
        System.out.println("Welcome oauthUser" + oAuth2User);
        System.out.println("**********************redirect*******************************");
        System.out.println(userRequest.getClientRegistration().getRedirectUri());
        System.out.println("******************************************************");
        try {
            oAuth2User = processOauth2User(userRequest, oAuth2User);
        } catch (OAuth2AuthenticationProcessingException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
        return oAuth2User;

    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        System.out.println("oAuth2UserInfo userId" + oAuth2UserInfo.getId());
        User user = User.builder()
                .authProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .providerId(oAuth2UserInfo.getId())
                .profileName(oAuth2UserInfo.getName())
                .fullName(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .password(" ")
                .role(Role.USER)
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
                .getByInfoType(infoType, oAuth2User.getAttributes());
        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from your provider");
        }

        Optional<User> optionalUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if (optionalUser.isEmpty()) {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        } else {
            user = optionalUser.get();
            updateExistingUser(user, oAuth2UserInfo);
            if (user.getAuthProvider()
                    .equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getAuthProvider() + " account. Please use your " + user.getAuthProvider() +
                        " account to login.");
            }
        }

        return com.kazama.SpringOAuth2.security.oauth2.UserPrincipal.create(user, oAuth2User.getAttributes());

    }

}
