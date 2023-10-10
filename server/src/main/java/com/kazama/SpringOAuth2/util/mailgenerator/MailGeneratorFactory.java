package com.kazama.SpringOAuth2.util.mailgenerator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.kazama.SpringOAuth2.model.MailType;

@Component
public class MailGeneratorFactory {
    private static Map<MailType, MailGenerator> mailGeneratorList = new HashMap<MailType, MailGenerator>();

    public static MailGenerator getMailGenerator(MailType mailType) {
        return mailGeneratorList.get(mailType);
    }

    public void register(MailType mailType, MailGenerator mailGenerator) {

        this.mailGeneratorList.put(mailType, mailGenerator);
    }

}
