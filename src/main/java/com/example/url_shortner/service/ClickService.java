package com.example.url_shortner.service;

import com.example.url_shortner.model.Click;
import com.example.url_shortner.model.Url;
import com.example.url_shortner.repository.ClickRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ClickService {

    private final ClickRepository clickRepo;

    public ClickService(ClickRepository clickRepo) {
        this.clickRepo = clickRepo;
    }

    public Click create(Url url){
        Click click = Click.builder()
                .date(Instant.now())
                .url(url)
                .build();
        return clickRepo.save(click);
    }
}
