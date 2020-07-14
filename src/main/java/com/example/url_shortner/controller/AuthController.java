package com.example.url_shortner.controller;

import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.ConfirmationToken;
import com.example.url_shortner.service.ConfirmationService;
import com.example.url_shortner.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Log4j2
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final ConfirmationService confirmService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public ModelAndView getRegistrationPage(ModelAndView mav) {
        mav.addObject("registerRq", new RegRqUser());
        mav.setViewName("registration");
        return mav;
    }

    @PostMapping("/registration")
    public RedirectView register(@ModelAttribute RegRqUser user, RedirectAttributes ra) {
        if (!user.getPassword().equals(user.getPasswordConfirm()))
            return new RedirectView("/registration?password");
        if (userService.isUserExist(user.getUsername()))
            return new RedirectView("/registration?error");
        confirmService.createConfirmationToken(user);
        ra.addAttribute("message", String.format("A verification email has been sent to: %s", user.getUsername()));
        return new RedirectView("/info");
    }

    @GetMapping("/confirm-account")
    public RedirectView confirmUserAccount(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmService.findByConfirmationToken(confirmationToken);
        userService.registerNewUser(token);
        confirmService.delete(token);
        return new RedirectView("/login?register");
    }
}

