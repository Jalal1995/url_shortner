package com.example.url_shortner.service;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.User;
import com.example.url_shortner.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashSet;

import static com.google.common.hash.Hashing.murmur3_32;

@Service
@Log4j2
@PropertySource("classpath:url.properties")
@RequiredArgsConstructor
public class UrlService {

    @Value("${url.prefix}")
    private String URL_PREFIX;

    private final UrlRepository urlRepo;

    public Url create(String fullUrl, User user) {
        String shortedUrl = murmur3_32().hashString(fullUrl, StandardCharsets.UTF_8).toString();
        Url url = Url.builder()
                .shortUrl(URL_PREFIX + shortedUrl)
                .fullUrl(fullUrl)
                .creationDate(Instant.now())
                .visitCount(0L)
                .isActive(true)
                .user(user)
                .clicks(new HashSet<>())
                .build();
        return urlRepo.save(url);
    }

    public Url find(String shortUrl) {
        Url url = urlRepo.findByShortUrl(URL_PREFIX + shortUrl)
                .orElseThrow(() -> new RuntimeException(String.format("no URL for: %s", shortUrl)));
        url.setVisitCount(url.getVisitCount() + 1);
        return urlRepo.save(url);
    }

    public boolean isUrlValid(String fullUrl) {
        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http", "https"}
        );
        return urlValidator.isValid(fullUrl);
    }
}
