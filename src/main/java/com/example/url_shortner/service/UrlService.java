package com.example.url_shortner.service;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.User;
import com.example.url_shortner.repository.UrlRepository;
import com.google.common.hash.Hashing;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static com.google.common.hash.Hashing.*;

@Service
@Log4j2
@PropertySource("classpath:url.properties")
public class UrlService {

    @Value("${url.prefix}")
    private String URL_PREFIX;

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url createAndSave(String fullUrl, User user) {
        String shortedUrl = murmur3_32().hashString(fullUrl, StandardCharsets.UTF_8).toString();
        log.info("shorted URL generated: " + shortedUrl);
        Url url = new Url();
        url.setShortUrl(URL_PREFIX + shortedUrl);
        url.setFullUrl(fullUrl);
        url.setCreationDate(Instant.now());
        url.setVisitCount(0L);
        url.setIsActive(true);
        url.setUser(user);
        return urlRepository.save(url);
    }

    public List<Url> findAll() {
        return urlRepository.findAll();
    }


    public Url findAndCount(String shortUrl) {
        Url url = urlRepository.findByShortUrl(URL_PREFIX + shortUrl)
                .orElseThrow(() -> new RuntimeException(String.format("There is no URL for : %s", shortUrl)));
        url.setVisitCount(url.getVisitCount() + 1);
        urlRepository.save(url);
        return url;
    }
}
