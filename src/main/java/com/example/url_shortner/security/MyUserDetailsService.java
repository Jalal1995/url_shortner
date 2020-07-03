package com.example.url_shortner.security;

import com.example.url_shortner.model.User;
import com.example.url_shortner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static UserDetails mapper(User user) {
        return new MyUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(MyUserDetailsService::mapper)
                .orElseThrow(()->new UsernameNotFoundException(
                        String.format("User `%s` not found", username)));
    }
}
