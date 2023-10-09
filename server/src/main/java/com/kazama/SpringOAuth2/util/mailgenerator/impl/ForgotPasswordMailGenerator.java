package com.kazama.SpringOAuth2.util.mailgenerator.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import com.kazama.SpringOAuth2.model.MailType;
import com.kazama.SpringOAuth2.model.User;
import com.kazama.SpringOAuth2.util.mailgenerator.MailGenerator;
import com.kazama.SpringOAuth2.util.mailgenerator.MailGeneratorFactory;

import jakarta.annotation.PostConstruct;

@Component
public class ForgotPasswordMailGenerator extends MailGenerator {

    private final Resource resource = new ClassPathResource("/static/html/passwordForgot.html");

    private final String subject = "ForgotPassword ? ";

    @Autowired
    private MailGeneratorFactory mailGeneratorFactory;

    @PostConstruct
    private void init() {
        mailGeneratorFactory.register(MailType.FORGOT_PASSWORD_MAIL, this);
    }

    @Override
    public String genMail(User toUser, Map<String, String> mailAttribute) throws IOException {
        String mailString = convertHTMLotString(this.resource);
        return mailString.replace("[ProfileName]",
                toUser.getProfileName()).replace("[Reset_Token]",
                        mailAttribute.get("[Reset_Token]"));
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

}
