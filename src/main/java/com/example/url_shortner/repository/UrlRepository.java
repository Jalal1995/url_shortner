package com.example.url_shortner.repository;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.UserInfo;
import com.example.url_shortner.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);
    Optional<Url> findByShortUrlAndIsActive(String shortUrl, boolean isActive);
    Page<Url> findAllByUser(UserInfo user, Pageable page);
}
