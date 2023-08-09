package com.kazama.jwt.config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kazama.jwt.config.Interceptor.RequestLimitInterceptor;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RequestLimitInterceptor requestLimitInterceptor;

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    // // TODO Auto-generated method stub
    // registry.addMapping("/**").allowedOrigins("http://localhost:3000",
    // "http://127.0.0.1:3000")
    // .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")

    // .allowCredentials(true);
    // }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLimitInterceptor).addPathPatterns("/api/v1/auth/forgotPassowrd");
    }

    @Bean
    public org.springframework.web.filter.CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://127.0.0.1:3000");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new org.springframework.web.filter.CorsFilter(source);
    }

}
