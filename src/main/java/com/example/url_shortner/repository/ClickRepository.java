package com.example.url_shortner.repository;

import com.example.url_shortner.model.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {

}
