package com.example.url_shortner.controller;


import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@RequestMapping("/")
@Log4j2
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/main")
    public String getMainPage() {
        return "main-page";
    }

    @GetMapping("/registration")
    public ModelAndView getRegistrationPage(ModelAndView mav) {
        mav.setViewName("registration");
        mav.addObject("registerRq", new RegRqUser());
        return mav;
    }

    @PostMapping("/registration")
    public RedirectView register(@ModelAttribute RegRqUser regReqUserDto) {
        if (!regReqUserDto.getPassword().equals(regReqUserDto.getPasswordConfirm()))
            throw new RuntimeException("password confirm doesn't match");
        boolean result = userService.registerNewUser(regReqUserDto);
        String url = result ? "/registration?success" : "/registration?error";
        return new RedirectView(url);
    }

    @GetMapping("/success")
    public String successFullMethod() {
        return "success";
    }
}
