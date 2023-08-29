package com.kazama.SpringOAuth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import com.kazama.SpringOAuth2.config.AppProperties;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@OpenAPIDefinition(info = @Info(title = "Spring-OAuth2-Autnentication-Template", version = "1.0.0"))
public class SpringOAuth2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringOAuth2Application.class, args);
	}

}
