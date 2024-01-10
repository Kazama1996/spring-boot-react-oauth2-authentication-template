package com.kazama.SpringOAuth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kazama.SpringOAuth2.config.Interceptor.RequestLimitInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RequestLimitInterceptor requestLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("Adding requestLimitInterceptor");
        // registry.addInterceptor(requestLimitInterceptor).addPathPatterns("/api/v1/auth/forgotPassword")
        // .addPathPatterns("/api/v1/users/private/currentuser");
        registry.addInterceptor(requestLimitInterceptor);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        // UrlBasedCorsConfigurationSource source = new
        // UrlBasedCorsConfigurationSource();
        // CorsConfiguration config = new CorsConfiguration();
        // config.addAllowedOrigin("http://localhost:3000");
        // config.addAllowedOrigin("127.0.0.1:3000");
        // config.addAllowedHeader("*");
        // config.addAllowedMethod("*");
        // config.setAllowCredentials(true);
        // config.addExposedHeader("Set-Cookie");
        // source.registerCorsConfiguration("/**", config);
        // CorsFilter corsFilter = new CorsFilter(source);
        FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(corsFilter());
        filterRegistrationBean.setOrder(-101);
        return filterRegistrationBean;

    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://127.0.0.1:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.addExposedHeader("Set-Cookie");
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}
