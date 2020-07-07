package com.example.url_shortner.service;

import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.ConfirmationToken;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.repository.ConfirmationTokenRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class CreateTokenService {

    private final ConfirmationTokenRepository tokenRepository;
    private final EmailSenderService emailSenderService;

    public CreateTokenService(ConfirmationTokenRepository tokenRepository, EmailSenderService emailSenderService) {
        this.tokenRepository = tokenRepository;
        this.emailSenderService = emailSenderService;
    }

    public void createToken(UserInfo user){
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        tokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("url.shortener.spring@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());
        emailSenderService.sendEmail(mailMessage);
    }

    public ConfirmationToken findByConfirmationToken(String confirmationToken) {
        return tokenRepository.findByConfirmationToken(confirmationToken);
    }
}
