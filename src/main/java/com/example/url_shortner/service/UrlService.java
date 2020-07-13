package com.example.url_shortner.service;

import com.example.url_shortner.exception.InvalidLinkException;
import com.example.url_shortner.exception.UrlNotFoundException;
import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.hash.Hashing.murmur3_32;

@Service
@Log4j2
@PropertySource("classpath:url.properties")
@RequiredArgsConstructor
@Transactional
public class UrlService {

    @Value("${url.prefix}")
    private String URL_PREFIX;

    private final UrlRepository urlRepo;

    public Url create(String fullUrl, UserInfo user) {
        if (!isUrlValid(fullUrl))
            throw new InvalidLinkException(String.format("Url is invalid: %s", fullUrl));
        String suffix =
                murmur3_32().hashString(fullUrl + user.getId(), StandardCharsets.UTF_8).toString();
        String shortUrl = URL_PREFIX + suffix;
        if (isUrlExists(shortUrl)) throw new InvalidLinkException("you have already shortened this link");

        Url url = Url.builder()
                .shortUrl(shortUrl)
                .fullUrl(fullUrl)
                .creationDate(Instant.now())
                .visitCount(0L)
                .isActive(true)
                .user(user)
                .visits(new ArrayList<>())
                .build();
        return urlRepo.save(url);
    }

    public Url find(String suffix) {
        Url url = urlRepo.findByShortUrlAndIsActive(URL_PREFIX + suffix, true)
                .orElseThrow(() -> new UrlNotFoundException(String.format("no url for: %s", URL_PREFIX + suffix)));
        url.setVisitCount(url.getVisitCount() + 1);
        return urlRepo.save(url);
    }

    public boolean isUrlValid(String fullUrl) {
        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http", "https"}
        );
        return urlValidator.isValid(fullUrl);
    }

    public Url findByShortUrl(String shortUrl) {
        return urlRepo.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException(String.format("no url for: %s", shortUrl)));
    }

    public void update(String shortUrl, boolean isActive) {
        Url url = findByShortUrl(shortUrl);
        url.setIsActive(isActive);
        urlRepo.save(url);
    }

    public Page<Url> findAll(UserInfo user, int page) {
        return urlRepo.findAllByUser(user, PageRequest.of(page, 4));
    }

    public boolean isUrlExists(String shortUrl) {
        return urlRepo.findByShortUrl(shortUrl).isPresent();
    }

    public List<Url> search(String keyword, Long id) {
        return urlRepo.search(keyword, id);
    }
}
