package com.example.url_shortner.security;

import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static UserDetails mapper(UserInfo user) {
        return new MyUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         UserDetails userDetails = userRepository.findByUsername(username)
                .map(MyUserDetailsService::mapper)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("UserInfo `%s` not found", username)));
         log.info(userDetails);
         return userDetails;
    }
}
