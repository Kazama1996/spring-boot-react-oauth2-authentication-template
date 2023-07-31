package com.kazama.jwt.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kazama.jwt.Security.JwtService;
import com.kazama.jwt.dao.UserRepository;
import com.kazama.jwt.dto.request.AuthRequest;
import com.kazama.jwt.dto.request.ForgotPasswordRequest;
import com.kazama.jwt.dto.request.LoginRequest;
import com.kazama.jwt.dto.response.AuthResponse;
import com.kazama.jwt.exception.AppException;
import com.kazama.jwt.model.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import static com.kazama.jwt.Security.Role.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    
    private final MailService mailService;

    public ResponseEntity<?> createUser(AuthRequest request ,HttpServletResponse response)
    {
        String password = request.getPassword();
        Instant current = Instant.now();
        User user = User.builder().fullName(request.getFullName()).profileName(request.getProfileName()).email(request.getEmail()).password(passwordEncoder.encode(password)).updateAt(current).role(USER).build();
        userRepository.save(user);
        String jwtToken = jwtService.genJwt(user);
        Cookie cookie = new Cookie("jwt", jwtToken);
        response.addCookie(cookie);
        AuthResponse responseBody = AuthResponse.builder().status(HttpStatus.CREATED).token(jwtToken).build();
        return ResponseEntity.ok().body(responseBody);
    }


    public ResponseEntity<?> authenticate(LoginRequest request, HttpServletResponse response){

        User targetUser = userRepository.findByEmail(request.getEmail()).orElseThrow();
        
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(targetUser.getUserId().toString(),request.getPassword()));
      
        
        String jwtToken = jwtService.genJwt(targetUser);

        Cookie cookie = new Cookie("jwt", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        AuthResponse responseBody = AuthResponse.builder().status(HttpStatus.OK).token(jwtToken).build();
        response.addCookie(cookie);
        return ResponseEntity.ok().body(responseBody);
    }

    public ResponseEntity<?> sendPasswordResetEmail(ForgotPasswordRequest reqBody)throws AppException{

       User targetUser = userRepository.findByEmail(reqBody.getEmail()).orElseThrow(()-> new AppException("Could not found this email : "+ reqBody.getEmail()));
       UUID uuid  = UUID.randomUUID();
       String resetToken  = targetUser.getUserId().toString()+uuid.toString();
       String encrpytToken= passwordEncoder.encode(resetToken);
       targetUser.setPasswordResetToken(encrpytToken);
       

       String subject = "Outsta9ram : Password reset token";
       String content = "ClickLink: http://127.0.0.1:8080/api/v1/auth/updatePassword/"+resetToken;
       String htmlContent = "<html><body><h1>Hello!</h1><p>This is an HTML email sent from JAVAMail</p></body></html>";
       String from = "customService@outsta9ram.io";
       mailService.sendMail( from, targetUser.getEmail(), subject, htmlContent);

        userRepository.save(targetUser);
        
       return ResponseEntity.ok().body("Send password token to"+targetUser.getEmail());
    
    }
}
