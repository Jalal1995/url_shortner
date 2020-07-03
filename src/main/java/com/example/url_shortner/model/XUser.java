//package com.example.url_shortner.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.Size;
//import java.util.Set;
//
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//public class XUser {
//
//    @Id
//    @Column(name = "user_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    @Column(name = "full_name")
//    @Size(min = 5, message = "*Your full name must have at least 5 characters")
//    @NotEmpty(message = "*Please provide a full name")
//    private String fullName;
//    @Email(message = "*Please provide a valid Email")
//    @NotEmpty(message = "*Please provide an email")
//    private String email;
//    @Column
//    @Size(min = 5, message = "*Your password must have at least 5 characters")
//    @NotEmpty(message = "*Please provide your password")
//    private String password;
//    @Column(name = "active")
//    private Boolean active;
//    private String roles;
//    @Transient
//    private String passwordConfirm;
//    @Transient
//    private final static String DELIMITER = ":";
//
//
//    public String[] getRoles() {
//        return roles == null || roles.isEmpty() ? new String[] {}:
//                roles.split(DELIMITER);
//    }
//
//    public XUser(String fullName, String email, String password, boolean active, String... roles) {
//        this.fullName = fullName;
//        this.email = email;
//        this.password = password;
//        this.active = active;
//        setRoles(roles);
//    }
//
//    public void setRoles(String... roles) {
//        this.roles = String.join(DELIMITER,roles);
//    }
//
//
//}
