package com.example.url_shortner.repository;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DbTest1 {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DbTest1(UrlRepository urlRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner dbTest() {
        return args -> {
            User user = new User();
            user.setFullName("f");
            user.setPassword(passwordEncoder.encode("f"));
            user.setRoles(new String[] {"USER"});
            user.setUsername("f");

//            User user = userRepository.findById(1L)
//                    .orElseThrow(() -> new RuntimeException("no user with id: 1")); //this way also working

            Url url = new Url();
            url.setFullUrl("https://www.youtube.com/watch?v=CWBnMBz-zZU&t=47s");
            url.setShortUrl("http://localhost:8080/s/ftest2fg");
            url.setCreationDate(Instant.now());
            url.setVisitCount(0L);
            url.setIsActive(true);

            url.getUsers().add(user);
            user.getUrls().add(url);
            userRepository.save(user);
        };
    }
}
