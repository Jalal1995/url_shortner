package com.example.url_shortner.config;

import com.example.url_shortner.service.MyUserDetailsService;
import com.example.url_shortner.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(
                        "/registration**",
                        "/js/**",
                        "/css/**",
                        "/img/**",
                        "/webjars/**").permitAll()
                .antMatchers("/", "/login", "/registration").permitAll()
                //.antMatchers("/home").authenticated()
                .antMatchers("/main-page").hasRole("USER")
                .anyRequest().authenticated();

       /* http
                .rememberMe();*/

        http
                .formLogin()
                .loginPage("/login")
                /*.usernameParameter("username")
                .passwordParameter("password")*/
//        .successForwardUrl("/after_login")
//        .failureForwardUrl("/login_failure")
//        .failureHandler() Exceptions handling
                .permitAll();



    }



 /*   @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").permitAll();

        http.authorizeRequests().antMatchers("/main-page").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");

        http.
                authorizeRequests()
                .antMatchers(
                        "/registration**",
                        "/js/**",
                        "/css/**",
                        "/img/**",
                        "/webjars/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
                .authenticated().and().csrf().disable().formLogin()
                .loginPage("/login").failureUrl("/login?error=true")
                .usernameParameter("user_name")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").and().exceptionHandling()
                .accessDeniedPage("/access-denied");
    }*/

    /*@Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/imag/**");
    }*/
}
