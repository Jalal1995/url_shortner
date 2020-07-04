package com.example.url_shortner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = "urls")
@ToString(exclude = "urls")
@NoArgsConstructor
public class User {

    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Url> urls = new HashSet<>();


}
