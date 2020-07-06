package com.example.url_shortner.controller;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.User;
import com.example.url_shortner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping({"/main", "/"})
    public ModelAndView getMainPage(Authentication auth, ModelAndView mav) {
        User user = userService.extractUserFromAuth(auth);
        List<Url> urls = user.getUrls();
        mav.addObject("urls", urls);
        mav.addObject("user", user);
        mav.setViewName("main-page");
        return mav;
    }

    @GetMapping("/landing")
    public ModelAndView successFullMethod(Authentication auth, ModelAndView mav) {
        User user = userService.extractUserFromAuth(auth);
        mav.addObject("user", user);
        mav.setViewName("landing");
        return mav;
    }
}
