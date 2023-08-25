package com.kazama.SpringOAuth2.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kazama.SpringOAuth2.model.User;
import com.kazama.SpringOAuth2.security.oauth2.CustomOAuth2UserService;
import com.kazama.SpringOAuth2.security.oauth2.UserPrincipal;
import com.kazama.SpringOAuth2.util.JWT.JwtService;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    private CustomUserDetailsService customUserDetailsService;

    private final List<String> shouldNotFilterMatchers = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    @Autowired
    public JWTFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {

        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        shouldNotFilterMatchers.add("/api/v1/auth");
        shouldNotFilterMatchers.add("/hello");
        shouldNotFilterMatchers.add("/login/oauth2");
        shouldNotFilterMatchers.add("/oatuh2/authorization");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            System.out.println(request.getRemoteAddr());
            System.out.println(request.getLocalPort());

            String jwt = getJWTFromRequest(request);
            System.out.println("-----------------doFilterInternal : jwt  -------------------------");
            System.out.println(jwt);
            System.out.println("--------------------------------------------");
            String userId = jwtService.extractUserId(jwt);

            System.out.println("-----------------doFilterInternal : userId ---------------------------");
            System.out.println(userId);
            System.out.println("--------------------------------------------");

            UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadByUserId(userId);
            System.out.println("-----------------doFilterInternal : userId ---------------------------");
            System.out.println(userPrincipal);
            System.out.println("--------------------------------------------");

            System.out.println(org.springframework.util.StringUtils.hasText(jwt));
            System.out.println(jwtService.isTokenValid(jwt, userPrincipal));

            if (org.springframework.util.StringUtils.hasText(jwt) && jwtService.isTokenValid(jwt, userPrincipal)) {
                System.out.println("success");
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, userPrincipal.getAuthorities());

                System.out.println("-----------------doFilterInternal : authentication ---------------------------");
                System.out.println(authentication);
                System.out.println("--------------------------------------------");
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                System.out.println(authentication);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String cookieName = "jwt";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return shouldNotFilterMatchers.stream().anyMatch(matcher -> path.startsWith(matcher));
    }
}
