package com.example.url_shortner.controller;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.Visit;
import com.example.url_shortner.repository.UrlRepository;
import com.example.url_shortner.repository.VisitRepository;
import com.example.url_shortner.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final UrlRepository urlRepository;
    private final VisitRepository visitRepository;

    @GetMapping
    public ModelAndView getVisitsPost(ModelAndView mav,
                                      @RequestParam String shortUrl,
                                      @RequestParam(defaultValue = "0") int page){
        Url url = urlService.findByShortUrl(shortUrl);
        Page<Visit> allByUrl = visitRepository.findAllByUrl(url, PageRequest.of(page, 4));
        int totalPage = allByUrl.getTotalPages() == 0 ? 1 : allByUrl.getTotalPages();

        mav.addObject("data",  visitRepository.findAllByUrl( url,PageRequest.of(page,4)));
        mav.addObject("totalPages", totalPage);
        mav.addObject("shortUrl", shortUrl);
        mav.addObject("currentPage", page);
        mav.addObject("url", url);
        mav.setViewName("visits");
        return mav;
    }

    @PostMapping("")
    public String getEnable(@RequestParam String shortUrl,
                                      @RequestParam String myRadio){

        log.info(shortUrl);
        Url url = urlService.findByShortUrl(shortUrl);
        log.info(url);
        boolean isActive = Boolean.parseBoolean(myRadio);
        log.info(isActive);
        url.setIsActive(isActive);
        urlRepository.save(url);
        return "redirect:main";
    }


}
