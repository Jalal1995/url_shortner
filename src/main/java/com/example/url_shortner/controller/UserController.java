package com.example.url_shortner.controller;


import com.example.url_shortner.dto.RegRqUser;
import com.example.url_shortner.model.ConfirmationToken;
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
        mav.setViewName("registration");
        mav.addObject("registerRq", new RegRqUser());
        return mav;
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute RegRqUser regReqUserDto, UserInfo user, ModelAndView modelAndView, RedirectAttributes rattrs) {

        if (!regReqUserDto.getPassword().equals(regReqUserDto.getPasswordConfirm()))
            throw new RuntimeException("password confirm doesn't match");
        boolean existingUser = userService.findByUsername(regReqUserDto.getUsername());
        log.info(existingUser);
        if(existingUser)
        {
            modelAndView.addObject("message","This email already exists!");
            modelAndView.setViewName("error");
        }else {
            tokenService.createToken(user);
            rattrs.addAttribute("email", user.getUsername());
        }
       /* boolean success = userService.registerNewUser(regReqUserDto);
        String redirect = success ? "/registration?success" : "/registration?error";*/
        return "redirect:successful-registration";
    }

    @RequestMapping(value = "/successful-registration", method = RequestMethod.GET)
    public ModelAndView getSuccessRegister(ModelAndView modelAndView,@ModelAttribute("email") String email){
        modelAndView.addObject("emailId", email);
        log.info(email);
        modelAndView.setViewName("successfulRegistration");
        return modelAndView;
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView,
                                           @RequestParam("token") String confirmationToken){

        ConfirmationToken token = tokenService.findByConfirmationToken(confirmationToken);
        if(token != null){
            UserInfo user = userService.findUser(token.getUser().getUsername());
            user.setEnabled(true);
            userService.registerNewUser(user);
            modelAndView.setViewName("accountVerified");
        }
        else
        {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }

    @GetMapping("/forgot")
    public String get_forgot_password() {
        return "forgot-password";
    }
}
