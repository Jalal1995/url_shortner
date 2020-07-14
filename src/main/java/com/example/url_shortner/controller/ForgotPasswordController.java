package com.example.url_shortner.controller;

import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.PasswordResetToken;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.service.ResetPasswordService;
import com.example.url_shortner.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final UserService userService;
    private final ResetPasswordService passService;

    @GetMapping("/forgot")
    public ModelAndView getForgotPage() {
        return new ModelAndView("forgot-password");
    }

    @PostMapping("/forgot")
    public RedirectView reset(@RequestParam String username, RedirectAttributes attributes) {
        if (!userService.isUserExist(username))
            return new RedirectView("/forgot?userNotFound");
        UserInfo user = userService.findByUsername(username);
        passService.resetPassword(user);
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
}
