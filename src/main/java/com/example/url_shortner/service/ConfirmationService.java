package com.example.url_shortner.service;

import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.ConfirmationToken;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@PropertySource("classpath:api.properties")
public class ConfirmationService {

    @Value("${app.url.prefix}")
    private String url_prefix;

    private final ConfirmationTokenRepository confirmTokenRepo;
    private final EmailService emailService;
    private final PasswordEncoder enc;

    public void createConfirmationToken(RegRqUser userRq) {
        ModelMapper modelMapper = new ModelMapper();
        UserInfo user = modelMapper.map(userRq, UserInfo.class);
        user.setPassword(enc.encode(userRq.getPassword()));
        user.setRoles(new String[]{"USER"});
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmTokenRepo.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String message =
                String.format("To confirm your account, please click here : %s/confirm-account?token=", url_prefix);
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("url.shortener.spring@gmail.com");
        mailMessage.setText(message + confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);
    }

    public Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken) {
        return confirmTokenRepo.findByConfirmationToken(confirmationToken);
    }
}
