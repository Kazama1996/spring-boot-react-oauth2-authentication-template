package com.kazama.jwt.service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import com.kazama.jwt.Security.JwtService;
import com.kazama.jwt.dao.PasswordResetTokenRepository;
import com.kazama.jwt.dao.UserRepository;
import com.kazama.jwt.dto.request.AuthRequest;
import com.kazama.jwt.dto.request.ForgotPasswordRequest;
import com.kazama.jwt.dto.request.LoginRequest;
import com.kazama.jwt.dto.response.AuthResponse;
import com.kazama.jwt.exception.AppException;
import com.kazama.jwt.model.PasswordResetToken;
import com.kazama.jwt.model.User;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import static com.kazama.jwt.Security.Role.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final MailService mailService;

    public ResponseEntity<?> createUser(AuthRequest request, HttpServletResponse response) {
        String password = request.getPassword();
        Instant current = Instant.now();
        User user = User.builder().fullName(request.getFullName()).profileName(request.getProfileName())
                .email(request.getEmail()).password(passwordEncoder.encode(password)).updateAt(current).role(USER)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.genJwt(user);
        Cookie cookie = new Cookie("jwt", jwtToken);
        response.addCookie(cookie);
        AuthResponse responseBody = AuthResponse.builder().status(HttpStatus.CREATED).token(jwtToken).build();
        return ResponseEntity.ok().body(responseBody);
    }

    public ResponseEntity<?> authenticate(LoginRequest request, HttpServletResponse response) {

        User targetUser = userRepository.findByEmail(request.getEmail()).orElseThrow();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(targetUser.getUserId().toString(), request.getPassword()));

        String jwtToken = jwtService.genJwt(targetUser);

        Cookie cookie = new Cookie("jwt", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        AuthResponse responseBody = AuthResponse.builder().status(HttpStatus.OK).token(jwtToken).build();
        response.addCookie(cookie);
        return ResponseEntity.ok().body(responseBody);
    }

    public ResponseEntity<?> sendPasswordResetEmail(ForgotPasswordRequest reqBody)
            throws AppException, MessagingException, IOException {
        ZoneId userTimeZone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(userTimeZone);
        User targetUser = userRepository.findByEmail(reqBody.getEmail())
                .orElseThrow(() -> new AppException("Could not found this email : " + reqBody.getEmail()));

        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByEmail(reqBody.getEmail());

        if (passwordResetToken == null) {
            passwordResetToken = PasswordResetToken.builder().email(targetUser.getEmail()).attemptCounter(1)
                    .isBlackList(false).token(token).build();

        } else {
            ZonedDateTime iat = passwordResetToken.getIat().withZoneSameInstant(userTimeZone);
            if (passwordResetToken.isBlackList()) {
                if (iat.plusDays(1)
                        .compareTo(ZonedDateTime.now(userTimeZone)) > 0) {
                    throw new AppException("Too many attempt");
                } else {
                    passwordResetToken.setAttemptCounter(1);
                    passwordResetToken.setBlackList(false);
                }
            } else {
                if (passwordResetToken.getAttemptCounter() < 2) {
                    passwordResetToken.setAttemptCounter(passwordResetToken.getAttemptCounter() + 1);

                } else {
                    passwordResetToken.setBlackList(true);
                }
            }

        }
        passwordResetToken.setIat(now.plusMinutes(10));

        passwordResetTokenRepository.save(passwordResetToken);

        mailService.sendMail(targetUser, passwordResetToken);

        return ResponseEntity.ok().body("Send password token to" + targetUser.getEmail());

    }

    public RedirectView verifyPasswordResetToken(String token) {
        PasswordResetToken validToken = passwordResetTokenRepository.findByToken(token);
        if (validToken == null) {
            return new RedirectView("rickroll");
        }
        return new RedirectView("http://127.0.0.1:3000");

    }
}
