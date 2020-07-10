package com.example.url_shortner.service;

import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.ConfirmationToken;
import com.example.url_shortner.model.PasswordResetToken;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.repository.ConfirmationTokenRepository;
import com.example.url_shortner.repository.PasswordTokenRepository;
import com.example.url_shortner.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTokenService {

    private final ConfirmationTokenRepository tokenRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final EmailSenderService emailSenderService;
    private final UserService userService;
    private final PasswordEncoder enc;

    public void createConfirmationToken(RegRqUser userRq){
        ModelMapper modelMapper = new ModelMapper();
        UserInfo user = modelMapper.map(userRq, UserInfo.class);
        user.setPassword(enc.encode(userRq.getPassword()));
        user.setRoles(new String[] {"USER"});
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        tokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String message = "To confirm your account, please click here : http://localhost:8080/confirm-account?token=";
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("url.shortener.spring@gmail.com");
        mailMessage.setText(message + confirmationToken.getConfirmationToken());
        emailSenderService.sendEmail(mailMessage);
    }

    public void createResetPasswordToken(UserInfo user){
        PasswordResetToken passwordResetToken = new PasswordResetToken(user);
        passwordTokenRepository.save(passwordResetToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String message = "To reset your password, please click here : http://localhost:8080/confirm-reset?token=";
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Reset password");
        mailMessage.setFrom("url.shortener.spring@gmail.com");
        mailMessage.setText(message + passwordResetToken.getToken());
        emailSenderService.sendEmail(mailMessage);
    }

    public Optional<PasswordResetToken> findByUsername(String username) {
        return passwordTokenRepository.findByUser(userService.findByUsername(username));
    }


    public Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken) {
        return tokenRepository.findByConfirmationToken(confirmationToken);
    }

    public Optional<PasswordResetToken> findByPasswordResetToken(String passwordResetToken) {
        return passwordTokenRepository.findByToken(passwordResetToken);
    }
}
