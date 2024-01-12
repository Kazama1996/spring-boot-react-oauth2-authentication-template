package com.kazama.SpringOAuth2.service;

import static com.kazama.SpringOAuth2.security.Role.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import org.yaml.snakeyaml.tokens.ValueToken;

import com.kazama.SpringOAuth2.dao.PasswordResetTokenRepository;
import com.kazama.SpringOAuth2.dao.UserRepository;
import com.kazama.SpringOAuth2.dto.request.AuthRequest;
import com.kazama.SpringOAuth2.dto.request.ForgotPasswordRequest;
import com.kazama.SpringOAuth2.dto.request.LoginRequest;
import com.kazama.SpringOAuth2.dto.request.PasswordResetTokenRequest;
import com.kazama.SpringOAuth2.dto.request.UpdatePasswordRequest;
import com.kazama.SpringOAuth2.dto.response.AuthResponse;
import com.kazama.SpringOAuth2.exception.AppException;
import com.kazama.SpringOAuth2.exception.InvalidTokenException;
import com.kazama.SpringOAuth2.model.MailType;
import com.kazama.SpringOAuth2.model.PasswordResetToken;
import com.kazama.SpringOAuth2.model.User;
import com.kazama.SpringOAuth2.security.AuthProvider;
import com.kazama.SpringOAuth2.service.MailService.MailService;
import com.kazama.SpringOAuth2.service.MailService.impl.AccountCenterMailService;
import com.kazama.SpringOAuth2.util.CookieUtils;
import com.kazama.SpringOAuth2.util.JWT.JwtService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
public class AuthService {
    private UserRepository userRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final MailService mailService;

    private final ZoneId userTimeZone = ZoneId.systemDefault();

    public AuthService(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository,
            PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService,
            AccountCenterMailService mailService) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }

    private Boolean isPasswordResetTokenValid(Optional<PasswordResetToken> passwordResetToken) {
        Boolean isPresent = passwordResetToken.isPresent();
        ZonedDateTime iat = passwordResetToken.get().getIat().withZoneSameInstant(userTimeZone);
        ZonedDateTime now = ZonedDateTime.now(userTimeZone);

        // Boolean isNotExpired = now.minusMinutes(6).isBefore(iat);
        Boolean isNotExpired = iat.plusMinutes(6).isAfter(now);
        return isPresent & isNotExpired;

    }

    private PasswordResetToken retrievePasswordResetToken(String token) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (!isPasswordResetTokenValid(passwordResetToken)) {
            passwordResetTokenRepository.deleteByToken(token);
            throw new InvalidTokenException(
                    "The password reset url is expire, please provide your email to get a new one.");
        }

        return passwordResetToken.get();
    }

    private User getPasswordResetTokenOwner(String token) {
        PasswordResetToken passwordResetToken = retrievePasswordResetToken(token);

        User reqUser = userRepository.findByEmail(passwordResetToken.getEmail())
                .orElseThrow(() -> new InvalidTokenException("Token is invalid"));
        return reqUser;
    }

    public ResponseEntity<?> createUser(AuthRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {

        if (userRepository.existsByemail(request.getEmail())) {
            throw new AppException("Email :" + request.getEmail() + " already exist");
        }

        if (userRepository.existsByProfileName(request.getProfileName())) {
            throw new AppException("ProfileName :" + request.getProfileName() + " already exist");
        }
        if (request.getPassword().length() < 8 || request.getPassword().length() > 14) {
            throw new AppException("password must be contains 8 ~14 characters");
        }

        String password = request.getPassword();
        ZonedDateTime now = ZonedDateTime.now(userTimeZone);
        User user = User.builder().fullName(request.getFullName()).profileName(request.getProfileName())
                .email(request.getEmail()).password(passwordEncoder.encode(password)).updateAt(now).role(USER)
                .authProvider(AuthProvider.local)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.genJwt(user);
        CookieUtils.addCookie(response, "jwt", jwtToken, 0);
        AuthResponse responseBody = AuthResponse.builder().token(jwtToken).build();
        return ResponseEntity.ok().body(responseBody);
    }

    public ResponseEntity<?> authenticate(LoginRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        // User targetUser = userRepository.findByEmail(request.getEmail())
        // .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtService.genJwt(authentication);

        // assignTokentoCookie("jwt", jwtToken, response);

        CookieUtils.addCookie(response, "jwt", jwtToken, 0);

        AuthResponse responseBody = AuthResponse.builder().token(jwtToken).build();

        return ResponseEntity.ok().body(responseBody);
    }

    public ResponseEntity<?> sendPasswordResetEmail(ForgotPasswordRequest reqBody)
            throws AppException, MessagingException, IOException {
        Map<String, String> mailAttributes = new HashMap<>();
        ZonedDateTime now = ZonedDateTime.now(userTimeZone);
        User targetUser = userRepository.findByEmail(reqBody.getEmail())
                .orElseThrow(() -> new AppException("Could not found this email : " + reqBody.getEmail()));

        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByEmail(reqBody.getEmail()).orElse(
                PasswordResetToken.builder().email(targetUser.getEmail()).iat(now).build());

        passwordResetToken.setIat(now);

        passwordResetToken.setToken(token);

        passwordResetTokenRepository.save(passwordResetToken);

        mailAttributes.put("[Reset_Token]", passwordResetToken.getToken());

        mailService.sendMail(MailType.FORGOT_PASSWORD_MAIL, targetUser, mailAttributes);

        return ResponseEntity.ok().body("Send password token to" + targetUser.getEmail());

    }

    @Transactional
    public ResponseEntity<?> getPasswordResetToken(PasswordResetTokenRequest request) {

        PasswordResetToken token = retrievePasswordResetToken(request.getToken());

        System.out.println("Token Retrive Success");

        return ResponseEntity.ok().body(token);
    }

    @Transactional
    public ResponseEntity<?> updatePassword(UpdatePasswordRequest reqBody) {

        ZonedDateTime now = ZonedDateTime.now(userTimeZone);

        // verify passwordReset token
        User reqUser = getPasswordResetTokenOwner(reqBody.getPasswordResetToken());

        reqUser.setPassword(passwordEncoder.encode(reqBody.getNewPassword()));
        reqUser.setUpdateAt(now);
        userRepository.save(reqUser);

        // delete the passwordReset token in the DB
        passwordResetTokenRepository.deleteByEmail(reqUser.getEmail());
        // redirect to the login page.
        return ResponseEntity.status(200).body("Your password update, Login again please ~");
    }
}
