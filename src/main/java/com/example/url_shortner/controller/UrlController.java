package com.example.url_shortner.controller;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.service.UrlService;
import com.example.url_shortner.service.UserService;
import com.example.url_shortner.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;
    private final UserService userService;
    private final VisitService visitService;

    @GetMapping("/tiny/{suffix}")
    public RedirectView getUrl(@PathVariable String suffix) {
        Url url = urlService.find(suffix);
        visitService.create(url);
        return new RedirectView(url.getFullUrl());
    }

    @PostMapping("/create")
    public ModelAndView create(@RequestParam String fullUrl,
                               Authentication auth,
                               ModelAndView mav){
        UserInfo user = userService.extractUserFromAuth(auth);
        Url url = urlService.create(fullUrl, user);
        mav.addObject("shortUrl", url.getShortUrl());
        mav.setViewName("new-url");
        return mav;
    }

    @PostMapping("/active")
    public RedirectView getEnable(@RequestParam String shortUrl,
                                  @RequestParam(value = "myRadio", required = false) String myRadio) {
        boolean isActive = myRadio != null;
        urlService.update(shortUrl, isActive);
        return new RedirectView(String.format("visit?shortUrl=%s", shortUrl));
    }
}
