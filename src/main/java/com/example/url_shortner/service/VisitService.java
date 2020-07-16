package com.example.url_shortner.service;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.Visit;
import com.example.url_shortner.repository.VisitRepository;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Log4j2
public class VisitService {

    private final VisitRepository visitRepo;
    private final UrlService urlService;
    private final DatabaseReader dbReader;

    private static final List<String> IP_HEADERS = Arrays.asList(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    );

    public VisitService(VisitRepository visitRepo, UrlService urlService) throws IOException {
        this.visitRepo = visitRepo;
        this.urlService = urlService;
        File database = new File("GeoLite2-City.mmdb");
        dbReader = new DatabaseReader.Builder(database).build();
    }

    public void create(Url url, HttpServletRequest req) throws IOException, GeoIp2Exception {

        String ipAddress =  IP_HEADERS.stream()
                .map(req::getHeader)
                .filter(Objects::nonNull)
                .filter(ip -> !ip.isEmpty() && !ip.equalsIgnoreCase("unknown"))
                .findFirst()
                .orElseGet(req::getRemoteAddr);
        log.info(ipAddress);

        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        CityResponse countryResponse = dbReader.city(inetAddress);
        UserAgent userAgent = UserAgent.parseUserAgentString(req.getHeader("User-Agent"));
        log.info("{} {} ", userAgent.getBrowser().name(), userAgent.getBrowser().getName());

        Visit visit = Visit.builder()
                .city(countryResponse.getCity().getName())
                .country(countryResponse.getCountry().getName())
                .ipAddress(ipAddress)
                .browser(userAgent.getBrowser().name())
                .browserVersion(userAgent.getBrowserVersion().getVersion())
                .operationSystem(userAgent.getOperatingSystem().getName())
                .date(Instant.now())
                .url(url)
                .build();
        visitRepo.save(visit);
    }

    public Page<Visit> findAll(String shortUrl, int page) {
        Url url = urlService.findByShortUrl(shortUrl);
        return visitRepo.findAllByUrl(url, PageRequest.of(page, 4));
    }
}
