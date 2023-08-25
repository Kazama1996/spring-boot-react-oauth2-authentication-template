package com.kazama.SpringOAuth2.service;

import java.io.BufferedReader;
import java.io.FileReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kazama.SpringOAuth2.model.PasswordResetToken;
import com.kazama.SpringOAuth2.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    private final Resource resource = new ClassPathResource("/static/html/passwordForgot.html");

    private final String From = "customService@outsta9ram.io";
    private final String subject = "Outsta9ram : Forgot Passowrd ?";

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private static String convertHTMLtoString(String filePath) throws java.io.IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static String genPasswordForgotMail(String filePath, User targetUser, PasswordResetToken token)
            throws java.io.IOException {
        String mailString = convertHTMLtoString(filePath);
        return mailString.replace("[ProfileName]", targetUser.getProfileName()).replace("[MY URL]",
                "http://localhost:8080/api/v1/auth/passwordReset/" + token.getToken());
    }

    public void sendMail(User targetUser, PasswordResetToken newToken) throws MessagingException, java.io.IOException {
        // SimpleMailMessage message = new SimpleMailMessage();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String content = genPasswordForgotMail(resource.getFile().getPath(), targetUser, newToken);

        helper.setFrom(From);
        helper.setTo(targetUser.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);

    }
}
