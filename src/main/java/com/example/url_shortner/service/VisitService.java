package com.example.url_shortner.service;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.Visit;
import com.example.url_shortner.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepo;
    private final UrlService urlService;

    public void create(Url url){
        Visit visit = Visit.builder()
                .date(Instant.now())
                .url(url)
                .build();
        visitRepo.save(visit);
    }

    public Page<Visit> findAll(String shortUrl, int page) {
        Url url = urlService.findByShortUrl(shortUrl);
        return visitRepo.findAllByUrl(url, PageRequest.of(page, 4));
    }
}
