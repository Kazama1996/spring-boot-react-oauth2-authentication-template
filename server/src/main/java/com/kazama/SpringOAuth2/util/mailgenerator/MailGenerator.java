package com.kazama.SpringOAuth2.util.mailgenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.io.Resource;

import com.kazama.SpringOAuth2.model.User;

public abstract class MailGenerator {

    protected static String convertHTMLotString(Resource resource) throws java.io.IOException {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = resource.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();

    }

    public String genMail(User toUser, Map<String, String> mailAttribute) throws IOException {
        throw new NotImplementedException(
                "NotImplementedException: You are not implement this function - MailGenerator.genMail().");
    }

    public String getSubject() {
        throw new NotImplementedException(
                "NotImplementedException: You are not implement this function - MailGenerator.genMail().");
    }
}
