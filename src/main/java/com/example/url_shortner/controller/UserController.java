package com.example.url_shortner.controller;


import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.service.UserService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
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
        boolean success = userService.registerNewUser(regReqUserDto);
        String redirect = success ? "/registration?success" : "/registration?error";
        return new RedirectView(redirect);
    }

    @GetMapping("/forgot")
    public String get_forgot_password() {
        return "forgot-password";
    }
}
