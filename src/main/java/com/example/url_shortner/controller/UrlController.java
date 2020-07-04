package com.example.url_shortner.controller;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.User;
import com.example.url_shortner.service.ClickService;
import com.example.url_shortner.service.UrlService;
import com.example.url_shortner.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
@Log4j2
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;
    private final UserService userService;
    private final ClickService clickService;

    @GetMapping("/tiny/{shortUrl}")
    public RedirectView getUrl(@PathVariable String shortUrl) {
        Url url = urlService.find(shortUrl);
        clickService.create(url);
        return new RedirectView(url.getFullUrl());
    }

    @PostMapping("/create")
    public ModelAndView create(@RequestParam String fullUrl,
                               Authentication auth,
                               ModelAndView mav) {
        boolean valid = urlService.isUrlValid(fullUrl);
        if (!valid) throw new RuntimeException(String.format("URL Invalid: %s", fullUrl));
        User user = userService.extractUserFromAuth(auth);
        Url url = urlService.create(fullUrl, user);
        mav.setViewName("success");
        mav.addObject("shortUrl", url.getShortUrl());
        return mav;
    }
}
