package com.example.url_shortner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InfoController {

    @GetMapping("/info")
    public ModelAndView getInfoPage(ModelAndView mav, @RequestParam String message) {
        mav.addObject("message", message);
        mav.setViewName("info");
        return mav;
    }

    @GetMapping("/errormessage")
    public ModelAndView getErrorPage(ModelAndView mav, @RequestParam String message) {
        mav.addObject("message", message);
        mav.setViewName("error");
        return mav;
    }
}
