package com.example.url_shortner.controller;

import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.ConfirmationToken;
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
        mav.setViewName("registration");
        mav.addObject("registerRq", new RegRqUser());
        return mav;
    }


    @GetMapping("/forgot")
    public ModelAndView get_forgot_password(ModelAndView mav) {
        mav.setViewName("forgot-password");
        mav.addObject("userRq", new RegRqUser());
        return mav;
    }


    @PostMapping("/registration")
    public String register(@ModelAttribute RegRqUser regReqUserDto, UserInfo user, ModelAndView modelAndView, RedirectAttributes rattr) {

        if (!regReqUserDto.getPassword().equals(regReqUserDto.getPasswordConfirm()))
            throw new RuntimeException("password confirm doesn't match");
        boolean existingUser = userService.findByUsername(regReqUserDto.getUsername());
        //log.info(existingUser);
        if (existingUser) {
            modelAndView.addObject("message", "This email already exists!");
            modelAndView.setViewName("error");
        } else {
            tokenService.createConfirmationToken(user,"To confirm your account, please click here :" +
                    " http://localhost:8080/confirm-account?token=");
            rattr.addAttribute("email", user.getUsername());
        }
        return "redirect:successful-registration";
    }

    @PostMapping("/forgot")
    public String reset(@ModelAttribute RegRqUser userRq, UserInfo user, ModelAndView modelAndView, RedirectAttributes rattrs) {
        System.out.println(userRq.getUsername() + " username");
        boolean existingUser = userService.findByUsername(userRq.getUsername());
        log.info(existingUser);
        if (existingUser) {
            tokenService.createResetPasswordToken(user,"To confirm your account, please click here :" +
                    " http://localhost:8080/confirm-account?token=");
            rattrs.addAttribute("email", user.getUsername());

        } else {
            modelAndView.addObject("message", "This email already exists!");
            modelAndView.setViewName("error");
        }
        return "redirect:successful-registration";

    }

    @RequestMapping(value = "/confirm-reset", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmReset(@ModelAttribute RegRqUser regReqUserDto, ModelAndView modelAndView,
                                     @RequestParam("token") String passwordResetToken) {


        PasswordResetToken token = tokenService.findByPasswordResetToken(passwordResetToken);
        if (token != null) {
            if (!regReqUserDto.getPassword().equals(regReqUserDto.getPasswordConfirm()))
                throw new RuntimeException("password confirm doesn't match");

            UserInfo user = userService.findUser(token.getUser().getUsername());
            userService.updateUser(user, regReqUserDto.getPassword());
            modelAndView.setViewName("reset");
        } else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }


    @RequestMapping(value = "/successful-registration", method = RequestMethod.GET)
    public ModelAndView getSuccessRegister(ModelAndView modelAndView, @ModelAttribute("email") String email) {
        modelAndView.addObject("emailId", email);
        log.info(email + "email");
        modelAndView.setViewName("successfulRegistration");
        return modelAndView;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView,
                                           @RequestParam("token") String confirmationToken) {

        ConfirmationToken token = tokenService.findByConfirmationToken(confirmationToken);
        if (token != null) {
            UserInfo user = userService.findUser(token.getUser().getUsername());
            user.setEnabled(true);
            userService.registerNewUser(user);
            modelAndView.setViewName("accountVerified");
        } else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }


}

