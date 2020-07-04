package com.example.url_shortner.service;


import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.User;
import com.example.url_shortner.repository.UserRepository;
import com.example.url_shortner.security.MyUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User `%s` not found", username)));
    }

    public boolean registerNewUser(RegRqUser regRqUserDto) {

        Optional<User> found = userRepository.findByUsername(regRqUserDto.getUsername());

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(regRqUserDto, User.class);
        user.setPassword(passwordEncoder.encode(regRqUserDto.getPassword()));
        user.setRoles(new String[] {"USER"});

        userRepository.save(user);
        return !found.isPresent();
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
    }

    public User findUserFromAuthentication(Authentication auth){
        Object principal = auth.getPrincipal();
        MyUserDetails userDetails = (MyUserDetails) principal;
        Long userId = userDetails.getId();
        return findById(userId);
    }
}
