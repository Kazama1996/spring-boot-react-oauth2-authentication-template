package com.kazama.SpringOAuth2.service.MailService.impl;

import com.kazama.SpringOAuth2.service.MailService.MailService;
import com.kazama.SpringOAuth2.util.mailgenerator.MailGenerator;
import com.kazama.SpringOAuth2.util.mailgenerator.MailGeneratorFactory;

import java.io.IOException;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.kazama.SpringOAuth2.model.MailType;
import com.kazama.SpringOAuth2.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class AccountCenterMailService implements MailService {

    private final JavaMailSender javaMailSender;

    private final String from = "AccountCenter@mail.com";

    public AccountCenterMailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(MailType mailType, User toUser, Map<String, String> mailAttribute)
            throws IOException, MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        MailGenerator mailGenerator = MailGeneratorFactory.getMailGenerator(mailType);
        String content = mailGenerator.genMail(toUser, mailAttribute);
        String subject = mailGenerator.getSubject();
        System.out.println("Content is :" + content);
        // Send Email
        helper.setFrom(this.from);
        helper.setTo(toUser.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);

    }

}
