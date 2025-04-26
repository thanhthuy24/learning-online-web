package com.htt.elearning.configs;

import com.htt.elearning.filter.JwtTokenFilter;
import com.htt.elearning.role.pojo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
@RequiredArgsConstructor
public class WebConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "api/users/login",
                                "api/users/register",
                                "api/users/register-account",
                                "api/teacher/**",
                                "api/token/")
                        .permitAll()

                        .requestMatchers(GET, "api/token/get-list-tokens").hasAnyRole(Role.ADMIN, Role.TEACHER, Role.USER)
                        .requestMatchers(POST, "/api/token").permitAll()
                        .requestMatchers(DELETE, "api/token/userId").permitAll()
                        .requestMatchers(DELETE, "api/token/remove-token").hasAnyRole(Role.USER)

                        .requestMatchers(GET, "api/teachers/all").permitAll()
                        .requestMatchers(GET, "api/teachers/user/").permitAll()
                        .requestMatchers(GET, "api/teachers/{teacherId}/get-teacherId").permitAll()
                        .requestMatchers(GET, "api/teachers/get-teacher-by-userId/**").permitAll()
                        .requestMatchers(GET, "/api/teachers/{teacherId}").hasAnyRole(Role.ADMIN)

//                        .requestMatchers(GET, "/api/teacher/get-information/").permitAll()

                        .requestMatchers(POST, "/api/teachers/**").hasAnyRole(Role.ADMIN)
                        .requestMatchers(PUT, "/api/teachers/**").hasAnyRole(Role.ADMIN)
                        .requestMatchers(DELETE, "/api/teachers/**").hasAnyRole(Role.ADMIN)

                        .requestMatchers(GET, "api/users/all-users").hasAnyRole(Role.ADMIN)
                        .requestMatchers(GET, "api/users/auth/").permitAll()
                        .requestMatchers(GET, "api/users/get-users/").hasAnyRole(Role.ADMIN)
//                        .requestMatchers(GET, "api/users/current-user").hasAnyRole(Role.ADMIN, Role.USER, Role.TEACHER)
                        .requestMatchers(GET, "/api/users/get-userId").permitAll()
                        .requestMatchers(GET, "/api/users/get-user/").permitAll()
                        .requestMatchers(GET, "/api/users/get-users-by-ids").permitAll()
                        .requestMatchers(GET, "/api/users/get-users/").hasRole(Role.ADMIN)
                        .requestMatchers(GET, "/api/users/growth/").hasRole(Role.ADMIN)

                        .requestMatchers(PATCH, "api/users/update-active/").hasAnyRole(Role.ADMIN)
                        .requestMatchers(PUT, "api/users/update-user/").hasAnyRole(Role.ADMIN, Role.USER, Role.TEACHER)
                        .requestMatchers(DELETE, "api/users/**").hasAnyRole(Role.ADMIN)

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
