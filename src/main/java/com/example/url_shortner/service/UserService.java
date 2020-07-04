package com.example.url_shortner.service;


import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.User;
import com.example.url_shortner.repository.UserRepository;
import com.example.url_shortner.security.MyUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public boolean registerNewUser(RegRqUser regRqUserDto) {
        Optional<User> found = userRepo.findByUsername(regRqUserDto.getUsername());
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(regRqUserDto, User.class);
        user.setPassword(encoder.encode(regRqUserDto.getPassword()));
        user.setRoles(new String[]{"USER"});
        userRepo.save(user);
        return !found.isPresent();
    }

    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));
    }

    public User extractUserFromAuth(Authentication auth) {
        Object principal = auth.getPrincipal();
        MyUserDetails userDetails = (MyUserDetails) principal;
        Long userId = userDetails.getId();
        return findById(userId);
    }
}
