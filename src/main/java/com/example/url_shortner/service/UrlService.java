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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static com.google.common.hash.Hashing.murmur3_32;

@Service
@Log4j2
@PropertySource("classpath:app.properties")
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
        Optional<Url> opUrl = findOpByShortUrl(shortUrl);
        if (!opUrl.isPresent()) {
            Url url = Url.builder()
                    .shortUrl(shortUrl)
                    .fullUrl(fullUrl)
                    .creationDate(Instant.now())
                    .visitCount(0L)
                    .isActive(true)
                    .user(user)
                    .visits(new ArrayList<>())
                    .expirationDate(ZonedDateTime.now(ZoneOffset.UTC).plusYears(1).toInstant())
                    .build();
            return urlRepo.save(url);
        } else {
            return opUrl.get();
        }
    }

    public Url find(String suffix) {
        Url url = urlRepo.findByShortUrlAndIsActive(URL_PREFIX + suffix, true)
                .orElseThrow(() -> new UrlNotFoundException(String.format("no url for: %s", URL_PREFIX + suffix)));
        url.setVisitCount(url.getVisitCount() + 1);
        return urlRepo.save(url);
    }

    public boolean isUrlValid(String fullUrl) {
        UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https", "magnet"});
        return urlValidator.isValid(fullUrl);
    }

    public Optional<Url> findOpByShortUrl(String shortUrl) {
        return urlRepo.findByShortUrl(shortUrl);
    }

    public Url findByShortUrl(String shortUrl) {
        return findOpByShortUrl(shortUrl)
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

    public List<Url> findAll() {
        return urlRepo.findAll();
    }


    public List<Url> search(String keyword, Long id) {
        return urlRepo.search(keyword, id);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleExpirationDate() {
        urlRepo.checkExpiration();
    }

    public void changeExpirationDate(String shortUrl, String date) throws ParseException {
        Url url = findByShortUrl(shortUrl);
        url.setExpirationDate(new SimpleDateFormat("yyyy-MM-dd").parse(date).toInstant());
        urlRepo.save(url);
    }
}
