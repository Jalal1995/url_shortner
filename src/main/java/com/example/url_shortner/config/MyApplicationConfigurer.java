package com.example.url_shortner.config;

import com.example.url_shortner.repository.UrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.stream.IntStream;

@Configuration
@EnableWebMvc
@EnableScheduling
@AllArgsConstructor
public class MyApplicationConfigurer implements WebMvcConfigurer {

    private final UrlRepository urlRepository;

    private static final String PREFIX = "classpath:/static";
    // web mappings
    private static final String[] MAPPINGS = {"/css/**", "/js/**", "/img/**"};
    // file system mapping
    private static final String[] LOCATIONS = {"/css/", "/js/", "/img/"};

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry reg) {
        if (MAPPINGS.length != LOCATIONS.length) throw new IllegalArgumentException(
                "Config: Mapping size must be equal to the Location size");
        IntStream.range(0, MAPPINGS.length)
                .forEach(idx -> reg.addResourceHandler(MAPPINGS[idx])
                        .addResourceLocations(PREFIX + LOCATIONS[idx])
                );
    }

    @Scheduled(cron = "0 52 15 * * *")
    public void scheduleFixedDelayTask() {
        urlRepository.checkTime();
    }
}
