package com.example.url_shortner.service;

import com.example.url_shortner.exception.InvalidLinkException;
import com.example.url_shortner.exception.TokenNotFoundException;
import com.example.url_shortner.model.PasswordResetToken;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.repository.PasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@PropertySource("classpath:app.properties")
public class ResetPasswordService {

    @Value("${app.url.prefix}")
    private String URL_PREFIX;

    @Value("${app.email.address}")
    private String APP_EMAIL;

    private final PasswordTokenRepository passTokenRepo;
    private final EmailService emailService;
    private final UserService userService;

    public void resetPassword(UserInfo user) {
        findOpTokenByUsername(user.getUsername()).ifPresent(this::delete);
        PasswordResetToken token = passTokenRepo.save(new PasswordResetToken(user));
        String message = String.format("To reset your password, please click here : %s/confirm-reset?token=%s",
                        URL_PREFIX, token.getToken());
        String subject = "Reset password";
        emailService.sendMailMessage(
                APP_EMAIL,
                user.getUsername(),
                subject,
                message
        );
    }

    public Optional<PasswordResetToken> findOpTokenByUsername(String username) {
        return passTokenRepo.findByUser(userService.findByUsername(username));
    }

    public PasswordResetToken findByUsername(String username) {
        return findOpTokenByUsername(username)
                .orElseThrow(() -> new TokenNotFoundException("token not found"));
    }

    public PasswordResetToken findByPasswordResetToken(String passwordResetToken) {
        return passTokenRepo.findByToken(passwordResetToken)
                .orElseThrow(() -> new InvalidLinkException("The link is invalid or broken!"));
    }

    public void delete(PasswordResetToken token) {
        token.setUser(null);
        passTokenRepo.save(token);
        passTokenRepo.delete(token);
    }
}
