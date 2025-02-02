package com.publicdrive.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.publicdrive.auth.JwtAuthenticationFilter;
import com.publicdrive.auth.JwtTokenUtil;


//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfiguration {
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//            .requestMatchers("/api/auth/register","/api/auth/login").permitAll() // Protect other routes
//            .anyRequest().authenticated()
//            .and()
//            .addFilter(new JwtAuthenticationFilter(jwtTokenUtil)); // Add JWT filter
//    }
//}

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;

    public SecurityConfig(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/**")  // Disable CSRF for these paths
                )
                .authorizeHttpRequests(authorizeHttpRequests ->
                    authorizeHttpRequests
                        .requestMatchers("/api/auth/**").permitAll()  // Allow public access to auth endpoints
                        .requestMatchers("/**").hasRole("USER")  // All other requests require USER role
                        .anyRequest().authenticated()  // Ensure all other requests are authenticated
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before the default filter
        return http.build();  // Return the configured HttpSecurity object as a SecurityFilterChain
    }
}

