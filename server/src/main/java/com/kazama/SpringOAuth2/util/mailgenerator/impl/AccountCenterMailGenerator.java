package com.kazama.SpringOAuth2.util.mailgenerator.impl;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.kazama.SpringOAuth2.model.PasswordResetToken;
import com.kazama.SpringOAuth2.model.User;
import com.kazama.SpringOAuth2.util.mailgenerator.MailGenerator;

@Component
public class AccountCenterMailGenerator extends MailGenerator {
    public final String from = "AccountCenter@mail.com";

    public String genForgotPasswordMail(User user, PasswordResetToken passwordResetToken) throws IOException {
        final Resource resource = new ClassPathResource("/static/html/passwordForgot.html");
        String mailString = convertHTMLotString(resource);
        return mailString.replace("[ProfileName]",
                user.getProfileName()).replace("[MY URL]",
                        "http://localhost:8080/api/v1/auth/passwordReset/" + passwordResetToken.getToken());
    }

    public String genUnlockAccountMail(User user, String unlockLink) throws IOException {
        final Resource resource = new ClassPathResource("/static/html/unlock.html");
        String mailString = convertHTMLotString(resource);
        return mailString.replace("[ProfileName]",
                user.getProfileName()).replace("[MY UnLockURL]",
                        "http://localhost:8080/api/v1/auth/unlock/" + unlockLink);
    }
}
