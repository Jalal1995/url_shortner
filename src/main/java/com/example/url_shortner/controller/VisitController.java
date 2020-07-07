package com.example.url_shortner.controller;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.Visit;
import com.example.url_shortner.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
@Log4j2
public class VisitController {

    private final UrlService urlService;

    @GetMapping("/visit")
    public ModelAndView getVisitsPost(@RequestParam String shortUrl, ModelAndView mav) {
        Url url = urlService.findByShortUrl(shortUrl);
        List<Visit> visits = url.getVisits();
        mav.addObject("url", url);
        mav.addObject("visits", visits);
        mav.setViewName("visits");
        return mav;
    }

}
