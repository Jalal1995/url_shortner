package com.example.url_shortner.repository;

import com.example.url_shortner.model.Role;
import com.example.url_shortner.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
