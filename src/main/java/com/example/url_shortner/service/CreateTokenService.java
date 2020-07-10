package com.example.url_shortner.service;

import com.example.url_shortner.model.ConfirmationToken;
import com.example.url_shortner.model.PasswordResetToken;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.repository.ConfirmationTokenRepository;
import com.example.url_shortner.repository.PasswordTokenRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateTokenService {

    private final ConfirmationTokenRepository tokenRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final EmailSenderService emailSenderService;

    public CreateTokenService(ConfirmationTokenRepository tokenRepository, PasswordTokenRepository passwordTokenRepository, EmailSenderService emailSenderService) {
        this.tokenRepository = tokenRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.emailSenderService = emailSenderService;
    }

    public void createConfirmationToken(UserInfo user, String message){
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        tokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("url.shortener.spring@gmail.com");
        mailMessage.setText(message + confirmationToken.getConfirmationToken());
        emailSenderService.sendEmail(mailMessage);
    }
    public void createResetPasswordToken(UserInfo user, String message){
        PasswordResetToken passwordResetToken = new PasswordResetToken(user);
        passwordTokenRepository.save(passwordResetToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("url.shortener.spring@gmail.com");
        mailMessage.setText(message + passwordResetToken.getToken());
        emailSenderService.sendEmail(mailMessage);
    }



    public ConfirmationToken findByConfirmationToken(String confirmationToken) {
        return tokenRepository.findByConfirmationToken(confirmationToken);
    }

    public PasswordResetToken findByPasswordResetToken(String passwordResetToken) {
        return passwordTokenRepository.findByPasswordResetToken(passwordResetToken);
    }
}
