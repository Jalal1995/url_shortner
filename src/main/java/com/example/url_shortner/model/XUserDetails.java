//package com.example.url_shortner.model;
//
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.stream.Collectors;
//
//@Getter
//public class XUserDetails implements UserDetails {
//
//
//    private final int id;
//    private final String fullName;
//    private final String email;
//    private final String password;
//    private final Boolean active;
//    private final String[] roles;
//
//    public XUserDetails(int id, String fullName, String email, String password, Boolean active, String[] roles) {
//        this.id = id;
//        this.fullName = fullName;
//        this.email = email;
//        this.password = password;
//        this.active = active;
//        this.roles = roles;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//
//        return Arrays.stream(roles)
//                .map(s -> "ROLE_" + s)
//                .map(s -> (GrantedAuthority) () -> s)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//}
