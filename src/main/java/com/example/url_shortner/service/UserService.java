package com.example.url_shortner.service;

import com.example.url_shortner.model.XUser;
import com.example.url_shortner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public Optional<XUser> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public XUser saveUser(XUser xuser) {
        xuser.setActive(true);
        xuser.setPassword(encoder.encode(xuser.getPassword()));
        xuser.setRoles("USER");

        return userRepository.save(xuser);
    }


}
