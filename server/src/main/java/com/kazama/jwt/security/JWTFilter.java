package com.kazama.jwt.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kazama.jwt.model.User;
import com.kazama.jwt.security.oauth2.CustomOAuth2UserService;
import com.kazama.jwt.security.oauth2.UserPrincipal;
import com.kazama.jwt.util.JWT.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private JwtService jwtService;

    private CustomUserDetailsService customUserDetailsService;

    private final List<String> shouldNotFilterMatchers = new ArrayList<>();

    public JWTFilter() {
        shouldNotFilterMatchers.add("/api/v1/auth");
        shouldNotFilterMatchers.add("/hello");
        shouldNotFilterMatchers.add("/login/oauth2");
        shouldNotFilterMatchers.add("/oatuh2/authorization");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        String cookieName = "jwt";
        String jwtToken = null;
        String userId;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                jwtToken = cookie.getValue();
                break;
            }
        }

        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        userId = jwtService.extractUserId(jwtToken);
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserPrincipal userPrincipal = this.customUserDetailsService.loadByUserId(userId);
            if (jwtService.isTokenValid(jwtToken, userPrincipal.getUserId())) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userPrincipal.getUserId(), null, userPrincipal.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        System.out.println("--------------------------------------");
        System.out.println(path);
        System.out.println("--------------------------------------");

        return shouldNotFilterMatchers.stream().anyMatch(matcher -> path.startsWith(matcher));
    }
}
