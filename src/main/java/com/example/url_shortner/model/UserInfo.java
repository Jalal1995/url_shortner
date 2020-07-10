package com.example.url_shortner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(exclude = "urls")
@ToString(exclude = "urls")
@NoArgsConstructor
@Table(name = "user")
public class UserInfo {

    private static final long serialVersionUID = -3520127269892525916L;

    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String username;

    private String password;

    private String roles;

    private boolean isEnabled;

    @Transient
    @JsonIgnore
    private final String ROLES_DELIMITER = ":";

    public UserInfo(String fullName, String username, String password, String[] roles) {
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Url> urls = new ArrayList<>();


}
