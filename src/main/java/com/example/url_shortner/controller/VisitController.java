package com.example.url_shortner.controller;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.Visit;
import com.example.url_shortner.repository.UrlRepository;
import com.example.url_shortner.repository.VisitRepository;
import com.example.url_shortner.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/visit")
@Log4j2
public class VisitController {

    private final UrlService urlService;
    private final VisitRepository visitRepository;

    @GetMapping
    public ModelAndView getVisitsPost(ModelAndView mav, Model model,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam String shortUrl){
        Url url = urlService.findByShortUrl(shortUrl);

        model.addAttribute("data",  visitRepository.findAllByUrl( url,PageRequest.of(page,4)));
        model.addAttribute("currentPage", page);


        List<Visit> visits = url.getVisits();
        mav.addObject("url", url);
        //mav.addObject("visits", visits);
        mav.setViewName("visits");
        return mav;
    }

}
