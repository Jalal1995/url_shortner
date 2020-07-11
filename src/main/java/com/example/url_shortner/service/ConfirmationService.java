package com.example.url_shortner.service;

import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.exception.InvalidLinkException;
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

@Service
@Transactional
@RequiredArgsConstructor
@PropertySource("classpath:api.properties")
public class ConfirmationService {

    @Value("${app.url.prefix}")
    private String URL_PREFIX;

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
                String.format("To confirm your account, please click here : %s/confirm-account?token=", URL_PREFIX);
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("url.shortener.spring@gmail.com");
        mailMessage.setText(message + confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);
    }

    public ConfirmationToken findByConfirmationToken(String confirmationToken) {
        return confirmTokenRepo.findByConfirmationToken(confirmationToken)
                .orElseThrow(() -> new InvalidLinkException("The link is invalid or broken!"));
    }

    public void delete(ConfirmationToken token) {
        token.setUser(null);
        confirmTokenRepo.save(token);
        confirmTokenRepo.delete(token);
    }
}
