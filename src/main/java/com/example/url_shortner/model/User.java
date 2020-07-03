package com.example.url_shortner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Column(name = "u_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotNull
    @Size(min = 1, message = "This field cannot be empty.")
    private String fullName;

    @Column(unique = true)
    @NotNull
    @Size(min = 1, message = "This field cannot be empty.")
    private String username;

    @NotNull
    @Size(min = 1, message = "This field cannot be empty.")
    private String password;

    private String roles;

    @Transient
    @JsonIgnore
    private final String ROLES_DELIMITER = ":";

    public User(String fullName, String username, String password, String[] roles) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        setRoles(roles);
    }

    public String[] getRoles() {
        if (this.roles == null || this.roles.isEmpty()) return new String[]{};
        return this.roles.split(ROLES_DELIMITER);
    }

    public void setRoles(String[] roles) {
        this.roles = String.join(ROLES_DELIMITER, roles);
    }
}
