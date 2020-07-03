package com.example.url_shortner.repository;

import com.example.url_shortner.model.XUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<XUser, Integer> {
    Optional<XUser> findByEmail(String email);
}
