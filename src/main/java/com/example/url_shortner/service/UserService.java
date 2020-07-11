package com.example.url_shortner.service;


import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.ConfirmationToken;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder enc;

    public void registerNewUser(ConfirmationToken token) {
        UserInfo user = findByUsername(token.getUser().getUsername());
        user.setEnabled(true);
        userRepo.save(user);

    }

    public UserInfo extractUserFromAuth(Authentication auth) {
        String username = auth.getName();
        return findByUsername(username);
    }

    public UserInfo findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("no user with username %s", username)));
    }

    public Optional<UserInfo> findOpUser(String username) {
        return userRepo.findByUsername(username);
    }

    public boolean isUserExists(String username) {
        return userRepo.findByUsername(username).isPresent();
    }

    public void save(UserInfo userInfo) {
        userRepo.save(userInfo);
    }

    public void update(RegRqUser userRq) {
        UserInfo user = findByUsername(userRq.getUsername());
        user.setPassword(enc.encode(userRq.getPassword()));
        save(user);
    }
}
