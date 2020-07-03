package com.example.url_shortner.controller;


import com.example.url_shortner.model.XUser;
import com.example.url_shortner.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        XUser XUser = new XUser();
        modelAndView.addObject("user", XUser);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid XUser XUser, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (userService.findUserByEmail(XUser.getEmail()).isPresent()) {
            bindingResult
                    .rejectValue("email", "error.XUser",
                            "There is already a XUser registered with the XUser name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {

            userService.saveUser(XUser);
            modelAndView.addObject("successMessage", "XUser has been registered successfully");
            modelAndView.addObject("user", new XUser());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }


    @RequestMapping(value="/main-page", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("main-page");
        return modelAndView;
    }



}
