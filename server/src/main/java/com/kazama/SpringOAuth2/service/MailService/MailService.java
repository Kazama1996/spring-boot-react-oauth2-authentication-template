package com.kazama.SpringOAuth2.service.MailService;

import java.io.IOException;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;

import com.kazama.SpringOAuth2.model.MailType;
import com.kazama.SpringOAuth2.model.User;

import jakarta.mail.MessagingException;

public interface MailService {

    public void sendMail(MailType mailType, User toUser, Map<String, String> mailAttribute)
            throws IOException, MessagingException;
}
