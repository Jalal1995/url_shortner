package com.example.url_shortner.controller;

import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.service.FacebookService;
import com.example.url_shortner.service.SecurityService;
import com.example.url_shortner.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class FacebookController {

    private final FacebookService facebookService;
    private final UserService userService;
    private final SecurityService securityService;

    @GetMapping("/facebooklogin")
    public RedirectView facebookLogin() {
        String url = facebookService.facebookLogin();
        return new RedirectView(url);
    }

    @GetMapping("/facebook")
    public RedirectView facebook(@RequestParam("code") String code) {
        String accessToken = facebookService.getFacebookAccessToken(code);
        return new RedirectView(String.format("/facebookprofiledata/%s", accessToken));
    }

    @GetMapping("/facebookprofiledata/{accessToken:.+}")
    public RedirectView facebookProfileData(@PathVariable String accessToken, Model model, HttpServletRequest req) {
        User user = facebookService.getFacebookUserProfile(accessToken);
        Optional<UserInfo> optionalUserDb = userService.findOpUser(user.getEmail());
        String role;
        UserInfo dbUser;
        if (optionalUserDb.isPresent()) {
            dbUser = optionalUserDb.get();
            dbUser.setFullName(String.format("%s %s", user.getFirstName(), user.getLastName()));
        } else {
            dbUser = new UserInfo();
            dbUser.setFullName(String.format("%s %s", user.getFirstName(), user.getLastName()));
            dbUser.setUsername(user.getEmail());
            dbUser.setRoles(new String[]{"USER"});
        }
        userService.save(dbUser);
        role = dbUser.getRoles()[0];
        model.addAttribute("user", dbUser);
        securityService.autoLogin(user.getEmail(), null, role, req);
        return new RedirectView("/main");
    }
}
