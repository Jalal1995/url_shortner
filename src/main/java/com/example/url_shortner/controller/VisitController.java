package com.example.url_shortner.controller;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.Visit;
import com.example.url_shortner.repository.VisitRepository;
import com.example.url_shortner.service.UrlService;
import com.example.url_shortner.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/visit")
@Log4j2
public class VisitController {

    private final UrlService urlService;
    private final VisitService visitService;

    @GetMapping
    public ModelAndView getVisits(ModelAndView mav,
                                      @RequestParam String shortUrl,
                                      @RequestParam(defaultValue = "0") int page) {
        Page<Visit> visits = visitService.findAll(shortUrl, page);
        int totalPage = visits.getTotalPages() == 0 ? 1 : visits.getTotalPages();
        mav.addObject("data", visits);
        mav.addObject("totalPages", totalPage);
        mav.addObject("currentPage", page);
        mav.addObject("url", urlService.findByShortUrl(shortUrl));
        mav.setViewName("visits");
        return mav;
    }
}
