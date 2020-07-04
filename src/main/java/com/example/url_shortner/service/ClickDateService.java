package com.example.url_shortner.service;

import com.example.url_shortner.model.ClickDates;
import com.example.url_shortner.model.Url;
import com.example.url_shortner.repository.ClickDatesRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;

@Service
public class ClickDateService {

    private final ClickDatesRepository datesRepository;

    public ClickDateService(ClickDatesRepository datesRepository) {
        this.datesRepository = datesRepository;
    }

    public ClickDates createClickDates(Url url){
        ClickDates dates = new ClickDates();
        dates.setClickDate(Instant.now());
        HashSet<ClickDates> allClickDates = new HashSet<>();
        allClickDates.add(dates);
        url.setDates(allClickDates);
        dates.setUrl(url);
        return datesRepository.save(dates);
    }
}
