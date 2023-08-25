package com.kazama.SpringOAuth2.util.JWT;

import java.security.Key;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.kazama.SpringOAuth2.model.User;
import com.kazama.SpringOAuth2.security.CustomUserDetailsService;
import com.kazama.SpringOAuth2.security.oauth2.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${env.jwtSecret}")
    private String secret;
    private final ZoneId userTimeZone = ZoneId.systemDefault();

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public String genJwt(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadUserByUsername(email);
        return genJwt(new HashMap<>(), userPrincipal.getUserId().toString());
    }

    public String genJwt(User user) {
        return genJwt(new HashMap<>(), user.getUserId().toString());
    }

    public String genJwt(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return genJwt(new HashMap<>(), userPrincipal.getUserId().toString());
    }

    public String genJwt(Map<String, Object> claims, String userId) {

        return Jwts.builder().setClaims(claims).setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000L))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsSolver) {
        final Claims claims = extractAllClaims(token);
        return claimsSolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserPrincipal userPrincipal) {
        final String username = extractUserId(token);
        System.out.println("username is :" + username);
        System.out.println(userPrincipal.getUserId());
        return (username.equals(userPrincipal.getUserId().toString())) && !isTokenExpire(token);
    }

    public boolean isTokenExpire(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUserId(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

}
