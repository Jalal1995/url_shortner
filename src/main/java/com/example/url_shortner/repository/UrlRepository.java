package com.example.url_shortner.repository;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortUrl(String shortUrl);

    Optional<Url> findByShortUrlAndIsActive(String shortUrl, boolean isActive);

    Page<Url> findAllByUser(UserInfo user, Pageable page);

    @Query("SELECT u FROM Url u WHERE CONCAT(u.shortUrl, u.fullUrl) LIKE %:keyword% AND u.user.id = :id")
    List<Url> search(@Param("keyword") String keyword, @Param("id") Long id);


    @Modifying
    @Query(value = "UPDATE URL SET is_active = 0 WHERE expiration_date <= NOW() AND is_active = 1", nativeQuery = true)
    @Transactional
    void checkTime();
}
