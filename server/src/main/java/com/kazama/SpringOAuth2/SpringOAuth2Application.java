package com.kazama.SpringOAuth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import com.kazama.SpringOAuth2.config.AppProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class SpringOAuth2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringOAuth2Application.class, args);
	}

}
