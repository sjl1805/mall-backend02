package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.utils.JwtUtils;
import com.example.security.JwtAuthenticationEntryPoint;
import com.example.security.JwtAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Lazy;

import com.example.service.impl.UsersServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final @Lazy UsersServiceImpl usersService;


    public SecurityConfig(JwtUtils jwtUtils, 
                         JwtAuthenticationEntryPoint unauthorizedHandler,
                         @Lazy UsersServiceImpl usersService) {
        this.jwtUtils = jwtUtils;
        this.unauthorizedHandler = unauthorizedHandler;
        this.usersService = usersService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/login", "/api/users/register").permitAll()
                .anyRequest().authenticated()
            )
            .cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(handling -> handling
                .authenticationEntryPoint(unauthorizedHandler)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, usersService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 