package com.example.url_shortner.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:api.properties")
public class FacebookService {

    @Value("${spring.social.facebook.app-id}")
    private String facebook_id;

    @Value("${spring.social.facebook.app-secret}")
    private String facebook_secret;

    @Value("${app.url.prefix}")
    private String app_url_prefix;

    private FacebookConnectionFactory createFacebookConnection() {
        return new FacebookConnectionFactory(facebook_id, facebook_secret);
    }

    public String facebookLogin() {
        OAuth2Parameters parameters = new OAuth2Parameters();
        parameters.setRedirectUri(app_url_prefix + "/facebook");
        parameters.setScope("public_profile, email");
        return createFacebookConnection().getOAuthOperations().buildAuthenticateUrl(parameters);
    }

    public String getFacebookAccessToken(String code) {
        return createFacebookConnection()
                .getOAuthOperations()
                .exchangeForAccess(code, app_url_prefix + "/facebook", null)
                .getAccessToken();
    }

    public User getFacebookUserProfile(String accessToken) {
        Facebook facebook = new FacebookTemplate(accessToken);
        String[] fields = {"id", "first_name", "last_name", "email"};
        return facebook.fetchObject("me", User.class, fields);
    }
}
