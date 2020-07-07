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
    private String facebookId;

    @Value("${spring.social.facebook.app-secret}")
    private String facebookSecret;

    @Value("${application.url.prefix}")
    private String APP_URL_PREFIX;

    private FacebookConnectionFactory createFacebookConnection() {
        return new FacebookConnectionFactory(facebookId, facebookSecret);
    }

    public String facebookLogin() {
        OAuth2Parameters parameters = new OAuth2Parameters();
        parameters.setRedirectUri(APP_URL_PREFIX + "/facebook");
        parameters.setScope("public_profile, email");
        return createFacebookConnection().getOAuthOperations().buildAuthenticateUrl(parameters);
    }

    public String getFacebookAccessToken(String code) {
        return createFacebookConnection()
                .getOAuthOperations()
                .exchangeForAccess(code, APP_URL_PREFIX + "/facebook", null)
                .getAccessToken();
    }

    public User getFacebookUserProfile(String accessToken) {
        Facebook facebook = new FacebookTemplate(accessToken);
        String[] fields = {"id", "first_name", "last_name", "email"};
        return facebook.fetchObject("me", User.class, fields);
    }
}
