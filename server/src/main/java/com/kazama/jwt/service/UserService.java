package com.kazama.jwt.service;

import static com.kazama.jwt.security.Role.*;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import com.kazama.jwt.dao.PasswordResetTokenRepository;
import com.kazama.jwt.dao.UserRepository;
import com.kazama.jwt.dto.request.AuthRequest;
import com.kazama.jwt.dto.request.ForgotPasswordRequest;
import com.kazama.jwt.dto.request.LoginRequest;
import com.kazama.jwt.dto.request.UpdatePasswordRequest;
import com.kazama.jwt.dto.response.AuthResponse;
import com.kazama.jwt.exception.AppException;
import com.kazama.jwt.exception.InvalidTokenException;
import com.kazama.jwt.model.PasswordResetToken;
import com.kazama.jwt.model.User;
import com.kazama.jwt.util.JWT.JwtService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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

    private final ZoneId userTimeZone = ZoneId.systemDefault();

    private PasswordResetToken passwordResetToken;

    private void assignTokentoCookie(String tokenName, String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(tokenName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    private PasswordResetToken isPasswordResetTokenValid(String token) {
        PasswordResetToken validToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token is Invalid"));
        ZonedDateTime iat = validToken.getIat().withZoneSameInstant(userTimeZone);
        ZonedDateTime now = ZonedDateTime.now(userTimeZone);
        if (iat.plusMinutes(10).compareTo(now) < 0) {
            throw new InvalidTokenException("Token is Expire");
        }
        return validToken;
    }

    private User findTokenOwner(String token) {
        PasswordResetToken passwordResetToken = isPasswordResetTokenValid(token);

        User reqUser = userRepository.findByEmail(passwordResetToken.getEmail())
                .orElseThrow(() -> new InvalidTokenException("Token is invalid"));
        return reqUser;
    }

    public ResponseEntity<?> createUser(AuthRequest request, HttpServletResponse response) {

        if (userRepository.existsByemail(request.getEmail())) {
            throw new AppException("Email :" + request.getEmail() + " already exist");
        }

        if (userRepository.existsByProfileName(request.getProfileName())) {
            throw new AppException("ProfileName :" + request.getProfileName() + " already exist");
        }

        String password = request.getPassword();
        ZonedDateTime now = ZonedDateTime.now(userTimeZone);
        User user = User.builder().fullName(request.getFullName()).profileName(request.getProfileName())
                .email(request.getEmail()).password(passwordEncoder.encode(password)).updateAt(now).role(USER)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.genJwt(user);
        assignTokentoCookie("jwt", jwtToken, response);
        AuthResponse responseBody = AuthResponse.builder().status(HttpStatus.CREATED).token(jwtToken).build();
        return ResponseEntity.ok().body(responseBody);
    }

    public ResponseEntity<?> authenticate(LoginRequest request, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User targetUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtService.genJwt(targetUser);

        assignTokentoCookie("jwt", jwtToken, response);

        AuthResponse responseBody = AuthResponse.builder().status(HttpStatus.OK).token(jwtToken).build();

        return ResponseEntity.ok().body(responseBody);
    }

    public ResponseEntity<?> sendPasswordResetEmail(ForgotPasswordRequest reqBody)
            throws AppException, MessagingException, IOException {
        ZonedDateTime now = ZonedDateTime.now(userTimeZone);
        User targetUser = userRepository.findByEmail(reqBody.getEmail())
                .orElseThrow(() -> new AppException("Could not found this email : " + reqBody.getEmail()));

        String token = UUID.randomUUID().toString();

        passwordResetToken = passwordResetTokenRepository.findByEmail(reqBody.getEmail()).orElse(
                PasswordResetToken.builder().email(targetUser.getEmail()).iat(now).build());

        // if (passwordResetToken == null) {
        // passwordResetToken =
        // PasswordResetToken.builder().email(targetUser.getEmail()).attemptCounter(1)
        // .isBlackList(false).token(token).build();

        // } else {
        // ZonedDateTime iat =
        // passwordResetToken.getIat().withZoneSameInstant(userTimeZone);
        // if (passwordResetToken.isBlackList()) {
        // if (iat.plusDays(1)
        // .compareTo(ZonedDateTime.now(userTimeZone)) > 0) {
        // throw new AppException("Too many attempt");
        // } else {
        // passwordResetToken.setAttemptCounter(1);
        // passwordResetToken.setBlackList(false);
        // }
        // } else {
        // passwordResetToken.setAttemptCounter(passwordResetToken.getAttemptCounter() +
        // 1);

        // if (passwordResetToken.getAttemptCounter() == 3) {
        // passwordResetToken.setBlackList(true);

        // }
        // }

        // }
        passwordResetToken.setIat(now.plusMinutes(10));

        passwordResetToken.setToken(token);

        passwordResetTokenRepository.save(passwordResetToken);

        mailService.sendMail(targetUser, passwordResetToken);

        return ResponseEntity.ok().body("Send password token to" + targetUser.getEmail());

    }

    public RedirectView redirectUpdatePasswordPage(String token) {
        PasswordResetToken passwordResetToken = isPasswordResetTokenValid(token);

        return new RedirectView("http://127.0.0.1:3000/resetPassword?token=" + passwordResetToken.getToken());
    }

    @Transactional
    public ResponseEntity<?> updatePassword(UpdatePasswordRequest reqBody) {

        ZonedDateTime now = ZonedDateTime.now(userTimeZone);

        // verify passwordReset token
        User reqUser = findTokenOwner(reqBody.getPasswordResetToken());

        reqUser.setPassword(passwordEncoder.encode(reqBody.getNewPassword()));
        reqUser.setUpdateAt(now);
        userRepository.save(reqUser);

        // delete the passwordReset token in the DB
        passwordResetTokenRepository.deleteByEmail(reqUser.getEmail());
        // redirect to the login page.
        return ResponseEntity.ok().body("Your password already update");
    }
}
