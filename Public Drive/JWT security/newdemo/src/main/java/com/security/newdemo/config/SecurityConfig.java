// THIS CLASS IS CONFIGURATION CLASS WHEREIN WE WILL CONFIGURE-FILTERCHAINS.
package com.security.newdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.security.newdemo.filter.CustomFilterForJWT;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomFilterForJWT jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  
            .cors().and()      
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/auth/**").permitAll() 
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic().disable()           
            .formLogin().disable()           
            .logout().disable();         

        // Add JWT filter before the default UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    

}
