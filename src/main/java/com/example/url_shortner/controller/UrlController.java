package com.example.url_shortner.controller;

import com.example.url_shortner.model.ClickDates;
import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.User;
import com.example.url_shortner.repository.ClickDatesRepository;
import com.example.url_shortner.repository.UrlRepository;
import com.example.url_shortner.security.MyUserDetails;
import com.example.url_shortner.service.ClickDateService;
import com.example.url_shortner.service.UrlService;
import com.example.url_shortner.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/")
@Log4j2
public class UrlController {

    private final UrlService urlService;
    private final UserService userService;
    private final ClickDateService dateService;

    public UrlController(UrlService urlService, ClickDateService dateService, UrlRepository urlRepository, UserService userService) {
        this.urlService = urlService;
        this.dateService = dateService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<Url> getAll() {
        return urlService.findAll();
    }

    @GetMapping("/s/{shortUrl}")
    public RedirectView getUrl(@PathVariable String shortUrl) {
        Url found = urlService.findAndCount(shortUrl);
        ClickDates clickDates = dateService.createClickDates(found);
        log.info(String.format("ClickDate for %s", clickDates));
        log.info(String.format("URL Retrieved: %s", found.getFullUrl()));
        return new RedirectView(found.getFullUrl());
    }

    @PostMapping("/create")
    public ModelAndView createAndSave(@RequestParam String fullUrl, Authentication auth) {
        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http", "https"}
        );
        if (!urlValidator.isValid(fullUrl)) throw new RuntimeException(String.format("URL Invalid: %s", fullUrl));

        User user = userService.findUserFromAuthentication(auth);
        Url savedUrl = urlService.createAndSave(fullUrl, user);
        user.getUrls().add(savedUrl);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("success");
        mav.addObject("shortUrl", savedUrl.getShortUrl());
        log.info(savedUrl.getShortUrl());

        return mav;
    }

  /*  private boolean urlIsValid(String fullUrl){
        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http", "https"}
        );
        return !urlValidator.isValid(fullUrl) ? true : false;
    }*/

}
