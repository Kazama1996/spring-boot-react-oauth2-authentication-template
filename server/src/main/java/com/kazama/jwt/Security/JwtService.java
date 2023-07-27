package com.kazama.jwt.Security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kazama.jwt.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${env.jwtSecret}")
    private String secret;
    
    public String genJwt(User user){
        return genJwt(new HashMap<>(), user);
    }

    public String genJwt(Map<String , Object> claims , User user){
        return Jwts.builder().setClaims(claims).setSubject(user.getUserId().toString()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+604800000L)).
        signWith(getSignInKey(),SignatureAlgorithm.HS256).compact();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T extractClaims(String token , Function<Claims, T> claimsSolver){
        final Claims claims = extractAllClaims(token);
        return claimsSolver.apply(claims);
    }

    public boolean isTokenValid(String token ,User user){
        final String userId = extractUserId(token);
        return user.getUserId().toString().equals(userId) && !isTokenExpire(token);
     }

    public boolean isTokenExpire(String token){
        return extractExpiration(token).before(new Date());
    }

  
    public String extractUserId(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private Date extractExpiration(String token){
        return extractClaims(token,Claims::getExpiration) ;
    }



}
