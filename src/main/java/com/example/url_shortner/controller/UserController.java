package com.example.url_shortner.controller;

import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.exception.InvalidLinkException;
import com.example.url_shortner.exception.TokenNotFoundException;
import com.example.url_shortner.model.PasswordResetToken;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.service.CreateTokenService;
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
    private final CreateTokenService tokenService;

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
    public RedirectView register(@ModelAttribute RegRqUser user,
                                 RedirectAttributes ra) {
        if (!user.getPassword().equals(user.getPasswordConfirm()))
            return new RedirectView("/registration?password");
        if (userService.isUserExists(user.getUsername()))
            return new RedirectView("/registration?error");
        tokenService.createConfirmationToken(user);
        ra.addAttribute("message", String.format("A verification email has been sent to: %s", user.getUsername()));
        return new RedirectView("/info");
    }

    @GetMapping("/forgot")
    public ModelAndView getForgotPage() {
        return new ModelAndView("forgot-password");
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

    @PostMapping("/forgot")
    public RedirectView reset(@RequestParam String username,
                              RedirectAttributes attributes) {
        if (!userService.isUserExists(username))
            return new RedirectView("/forgot?userNotFound");
        UserInfo user = userService.findByUsername(username);
        tokenService.createResetPasswordToken(user);
        attributes.addAttribute("message", String.format("An email has been sent to: %s for reset password", user.getUsername()));
        return new RedirectView("/info");
    }

    @GetMapping("/confirm-reset")
    public ModelAndView confirmReset(@RequestParam("token") String passwordResetToken, ModelAndView mav) {
        PasswordResetToken token = tokenService.findByPasswordResetToken(passwordResetToken)
                .orElseThrow(() -> new InvalidLinkException("The link is invalid or broken!"));
        RegRqUser user = new RegRqUser();
        user.setUsername(token.getUser().getUsername());
        mav.addObject("user", user);
        mav.setViewName("reset");
        return mav;
    }

    @PostMapping("/reset-password")
    public RedirectView resetPassword(@ModelAttribute RegRqUser user) {
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            PasswordResetToken token = tokenService.findByUsername(user.getUsername())
                    .orElseThrow(() -> new TokenNotFoundException("token not found"));
            return new RedirectView(String.format("/confirm-reset?token=%s", token.getToken()));
        }
        userService.update(user);
        return new RedirectView("/login?reset");
    }

    @GetMapping("/confirm-account")
    public RedirectView confirmUserAccount(@RequestParam("token") String confirmationToken) {
        userService.registerNewUser(tokenService.findByConfirmationToken(confirmationToken)
                .orElseThrow(() -> new InvalidLinkException("The link is invalid or broken!")));
        return new RedirectView("/login?register");
    }
}

