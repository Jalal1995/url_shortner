package com.example.url_shortner.controller;

import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.ConfirmationToken;
import com.example.url_shortner.model.PasswordResetToken;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.service.ConfirmationService;
import com.example.url_shortner.service.ResetPasswordService;
import com.example.url_shortner.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
@Log4j2
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResetPasswordService passService;
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
        if (userService.isUserExists(user.getUsername()))
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

    @GetMapping("/forgot")
    public ModelAndView getForgotPage() {
        return new ModelAndView("forgot-password");
    }

    @PostMapping("/forgot")
    public RedirectView reset(@RequestParam String username, RedirectAttributes attributes) {
        if (!userService.isUserExists(username))
            return new RedirectView("/forgot?userNotFound");
        UserInfo user = userService.findByUsername(username);
        passService.createResetPasswordToken(user);
        attributes.addAttribute("message", String.format("An email has been sent to: %s for reset password", user.getUsername()));
        return new RedirectView("/info");
    }

    @GetMapping("/confirm-reset")
    public ModelAndView confirmReset(@RequestParam("token") String passwordResetToken,
                                     ModelAndView mav) {
        PasswordResetToken token = passService.findByPasswordResetToken(passwordResetToken);
        RegRqUser user = new RegRqUser();
        user.setUsername(token.getUser().getUsername());
        mav.addObject("user", user);
        mav.setViewName("reset");
        return mav;
    }

    @PostMapping("/reset-password")
    public RedirectView resetPassword(@ModelAttribute RegRqUser user) {
        PasswordResetToken token = passService.findByUsername(user.getUsername());
        if (!user.getPassword().equals(user.getPasswordConfirm()))
            return new RedirectView(String.format("/confirm-reset?token=%s", token.getToken()));
        userService.update(user);
        passService.delete(token);
        return new RedirectView("/login?reset");
    }

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

