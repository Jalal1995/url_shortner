package com.example.url_shortner.repository;

import com.example.url_shortner.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
}
