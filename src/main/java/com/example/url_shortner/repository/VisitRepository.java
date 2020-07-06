package com.example.url_shortner.repository;

import com.example.url_shortner.model.Url;
import com.example.url_shortner.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findAllByUrl(Url url);
}
