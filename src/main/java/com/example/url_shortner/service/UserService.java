package com.example.url_shortner.service;


import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public boolean registerNewUser(UserInfo user) {
        Optional<UserInfo> found = userRepo.findByUsername(user.getUsername());
        /*ModelMapper modelMapper = new ModelMapper();
        UserInfo user = modelMapper.map(regRqUserDto, UserInfo.class);*/
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRoles(new String[]{"USER"});
        userRepo.save(user);
        return !found.isPresent();
    }

    public UserInfo extractUserFromAuth(Authentication auth) {
        String username = auth.getName();
        return findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("no user with username %s", username)));
    }

    public Optional<UserInfo> findByEmail(String email) {
        return userRepo.findByUsername(email);
    }

    public boolean findByUsername(String username) {
        return userRepo.findByUsername(username).isPresent();
                /*.orElseThrow(() -> new UsernameNotFoundException(
                String.format("User `%s` not found", email)));*/
    }

    public UserInfo findUser(String username) {
        return userRepo.findByUsername(username).get();

    }

    public void update(UserInfo dbUser) {
        userRepo.save(dbUser);
    }

    public UserInfo save(UserInfo userInfo) {
        if (StringUtils.hasText(userInfo.getPassword())) {
            userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        }
        return userRepo.save(userInfo);
    }
}
