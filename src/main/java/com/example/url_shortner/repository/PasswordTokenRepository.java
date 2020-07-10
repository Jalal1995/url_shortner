package com.example.url_shortner.repository;

import com.example.url_shortner.model.ConfirmationToken;
import com.example.url_shortner.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, String> {
    PasswordResetToken findByPasswordResetToken(String passwordResetToken);
}
