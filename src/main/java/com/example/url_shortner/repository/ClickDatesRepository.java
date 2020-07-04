package com.example.url_shortner.repository;

import com.example.url_shortner.model.ClickDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickDatesRepository extends JpaRepository<ClickDates, Long> {

}
