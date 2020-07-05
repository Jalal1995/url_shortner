package com.example.url_shortner.exception.handler;

import com.example.url_shortner.exception.UrlNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({UrlNotFoundException.class})
    public ModelAndView handleUrlNotFoundException(UrlNotFoundException ex) {
        ModelAndView mav = new ModelAndView();
        log.warn(String.format("exception caught: %s", ex.getMessage()));
        mav.setViewName("url-not-found-ex");
        return mav;
    }
}
