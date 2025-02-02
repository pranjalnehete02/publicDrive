package com.publicdrive.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import com.publicdrive.auth.JwtAuthenticationFilter;
import com.publicdrive.auth.JwtTokenUtil;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .requestMatchers("/login").permitAll() // Protect other routes
            .anyRequest().authenticated()
            .and()
            .addFilter(new JwtAuthenticationFilter(jwtTokenUtil)); // Add JWT filter
    }
}

