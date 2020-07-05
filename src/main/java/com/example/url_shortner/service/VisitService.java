package com.example.url_shortner.service;

import com.example.url_shortner.model.Visit;
import com.example.url_shortner.model.Url;
import com.example.url_shortner.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class VisitService {

    private final VisitRepository visitRepo;

    public VisitService(VisitRepository visitRepo) {
        this.visitRepo = visitRepo;
    }

    public Visit create(Url url){
        Visit visit = Visit.builder()
                .date(Instant.now())
                .url(url)
                .build();
        return visitRepo.save(visit);
    }
}
