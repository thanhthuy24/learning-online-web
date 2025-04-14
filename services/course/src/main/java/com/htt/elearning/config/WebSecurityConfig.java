package com.htt.elearning.config;

import com.htt.elearning.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/category/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "api/categories/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/courses/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/course/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/lessons/count-by-course/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/lessons/auth/course/").hasRole("USER, TEACHER")

                        .requestMatchers(POST, "/api/video-completed/").hasRole("USER")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
