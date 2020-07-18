package com.example.url_shortner.controller;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.repository.UrlRepository;
import com.example.url_shortner.service.UrlService;
import com.example.url_shortner.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final UrlService urlService;

    @GetMapping({"/main", "/"})
    public ModelAndView getMainPage(Authentication auth, ModelAndView mav,
                                    @RequestParam(defaultValue = "1") int page) {
        UserInfo user = userService.extractUserFromAuth(auth);
        Page<Url> urls = urlService.findAll(user, page - 1);
        int totalPage = Math.max(urls.getTotalPages(), 1);
        mav.addObject("data", urls);
        mav.addObject("totalPages", totalPage);
        mav.addObject("currentPage", page);
        mav.addObject("user", user);
        mav.setViewName("main-page");
        return mav;
    }

    @GetMapping("/landing")
    public ModelAndView successFullMethod(Authentication auth, ModelAndView mav) {
        UserInfo user = userService.extractUserFromAuth(auth);
        mav.addObject("user", user);
        mav.setViewName("landing");
        return mav;
    }
}
