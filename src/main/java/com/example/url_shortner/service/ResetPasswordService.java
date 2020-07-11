package com.example.url_shortner.service;

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
@PropertySource("classpath:api.properties")
public class ResetPasswordService {

    @Value("${app.url.prefix}")
    private String url_prefix;

    private final PasswordTokenRepository passTokenRepo;
    private final EmailService emailService;
    private final UserService userService;

    public void createResetPasswordToken(UserInfo user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user);
        passTokenRepo.save(passwordResetToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String message =
                String.format("To reset your password, please click here : %s/confirm-reset?token=", url_prefix);
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Reset password");
        mailMessage.setFrom("url.shortener.spring@gmail.com");
        mailMessage.setText(message + passwordResetToken.getToken());
        emailService.sendEmail(mailMessage);
    }

    public Optional<PasswordResetToken> findByUsername(String username) {
        return passTokenRepo.findByUser(userService.findByUsername(username));
    }

    public Optional<PasswordResetToken> findByPasswordResetToken(String passwordResetToken) {
        return passTokenRepo.findByToken(passwordResetToken);
    }
}
