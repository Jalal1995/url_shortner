package com.example.url_shortner.service;

import com.example.url_shortner.model.XUser;
import com.example.url_shortner.model.XUserDetails;
import com.example.url_shortner.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Configuration
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public MyUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    public static UserDetails mapper_to_standard_ud(XUser xuser){
        return User
                .withUsername(xuser.getEmail())
                .password(xuser.getPassword())
                .roles(xuser.getRoles())
                .build();
    }

    public static UserDetails mapper_to_xUserDetails(XUser xuser){
        return new XUserDetails(
                xuser.getId(),
                xuser.getFullName(),
                xuser.getEmail(),
                xuser.getPassword(),
                xuser.getActive(),
                xuser.getRoles()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info(String.format(">>>> loading user details for user: %s", email));

        return repository.findByEmail(email)
                .map(MyUserDetailsService::mapper_to_xUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User: %s isn't found in our DB", email)
                ));
    }

    /*@Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        XUser user = userService.findUserByEmail(email);
        List<GrantedAuthority> authorities = getUserAuthority((Set<Role>) user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (Role role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(XUser user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.XUser(user.getFullName(), user.getPassword(),
                user.getActive(), true, true, true, authorities);
    }*/

}
