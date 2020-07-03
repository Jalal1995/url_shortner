package com.example.url_shortner.controller;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.service.UrlService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/")
@Log4j2
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/all")
    public List<Url> getAll() {
        return urlService.findAll();
    }

    @GetMapping("/s/{shortUrl}")
    public RedirectView getUrl(@PathVariable String shortUrl) {
        Url found = urlService.findAndCount(shortUrl);
        log.info(String.format("URL Retrieved: %s", found.getFullUrl()));
        return new RedirectView(found.getFullUrl());
    }

    @PostMapping("/create")
    public ModelAndView createAndSave(@RequestParam String fullUrl) {

        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http", "https"}
        );
        if (!urlValidator.isValid(fullUrl)) throw new RuntimeException(String.format("URL Invalid: %s", fullUrl));
        Url savedUrl = urlService.createAndSave(fullUrl);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("success");
        mav.addObject("shortUrl", savedUrl.getShortUrl());
        log.info(savedUrl.getShortUrl());
        return mav;
    }
}
