package com.example.Event.Management.System.Config;

import com.example.Event.Management.System.Service.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Password Encoder Bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication Provider Bean
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home", "/register", "/save-user", "/css/**", "/js/**").permitAll()
                .requestMatchers("/admin/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
            .loginPage("/login")
            .loginProcessingUrl("/do-login")
            .successHandler((request, response, authentication) -> {
                
                String email = authentication.getName();
                request.getSession().setAttribute("username", email);
                if(email.equalsIgnoreCase("admin@gmail.com")) {
                    response.sendRedirect("/admin/events");
                } else {
                    response.sendRedirect("/user/dashboard");
                }
            })
            .permitAll());

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}
