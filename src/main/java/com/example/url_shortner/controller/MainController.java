package com.example.url_shortner.controller;

import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.repository.UrlRepository;
import com.example.url_shortner.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final UrlRepository urlRepository;

    @GetMapping({"/main", "/"})
    public ModelAndView getMainPage(Authentication auth, ModelAndView mav, Model model,
                                    @RequestParam(defaultValue = "0") int page) {
        UserInfo user = userService.extractUserFromAuth(auth);
        model.addAttribute("data", urlRepository.findAllByUser(user, PageRequest.of(page, 4)));
        model.addAttribute("currentPage", page);
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
