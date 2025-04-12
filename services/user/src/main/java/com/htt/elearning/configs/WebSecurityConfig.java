package com.htt.elearning.configs;

import com.htt.elearning.filter.JwtTokenFilter;
import com.htt.elearning.role.pojo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.HttpMethod.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    "api/users/login",
                                    "api/users/register",
                                    "api/users/register-account")
                            .permitAll()

                            .requestMatchers("/admin/**").hasAnyRole(Role.ADMIN)   // Chỉ Admin truy cập được
                            .requestMatchers("/teacher/**").hasAnyRole(Role.TEACHER) // Chỉ Teacher truy cập được

                            .requestMatchers(GET, "api/ai-recommend/").hasAnyRole(Role.USER)

                            .requestMatchers(GET, "api/assignments?**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(POST, "api/assignments/**").hasAnyRole(Role.ADMIN, Role.TEACHER)
                            .requestMatchers(PUT, "api/assignments/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(DELETE, "api/assignments/**").hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET, "api/assignments/**").hasAnyRole(Role.ADMIN, Role.TEACHER)
                            .requestMatchers(GET, "api/assignments/lesson/**").permitAll()

                            .requestMatchers(GET, "api/assignment-done/**").hasAnyRole(Role.USER, Role.TEACHER)
                            .requestMatchers(POST, "api/assignment-done/**").hasAnyRole(Role.USER)

                            .requestMatchers(GET, "api/answer-choices").hasAnyRole(Role.USER, Role.TEACHER)
                            .requestMatchers(POST, "api/answer-choices/**").hasAnyRole(Role.USER)

                            .requestMatchers(GET, "api/categories/**").permitAll()
                            .requestMatchers(POST, "api/categories/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(PUT, "api/categories/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(DELETE, "api/categories/**").hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET, "api/courses/**").permitAll()
                            .requestMatchers(POST, "api/courses/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(PUT, "api/courses/**").hasAnyRole(Role.ADMIN, Role.TEACHER)
                            .requestMatchers(DELETE, "api/courses/**").hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET, "api/course-teacher/").hasAnyRole(Role.TEACHER)

                            .requestMatchers(GET, "api/certificate/**").hasAnyRole(Role.ADMIN, Role.USER)

//                            .requestMatchers(GET, "api/rating/**").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(GET, "api/rating/**").permitAll()
                            .requestMatchers(POST, "api/rating/**").hasAnyRole(Role.USER)

                            .requestMatchers(POST, "api/choices/**").hasAnyRole(Role.TEACHER, Role.ADMIN)
                            .requestMatchers(PUT, "api/choices/**").hasAnyRole(Role.TEACHER, Role.ADMIN)

                            .requestMatchers(GET, "api/comments/**").hasAnyRole(Role.USER, Role.TEACHER, Role.ADMIN)
                            .requestMatchers(POST, "api/comments").hasAnyRole(Role.USER, Role.TEACHER)

                            .requestMatchers(GET, "api/get-notification").hasAnyRole(Role.USER)
                            .requestMatchers(POST, "api/get-notification").hasAnyRole(Role.ADMIN, Role.TEACHER)
                            .requestMatchers(PATCH, "api/get-notification/**").hasAnyRole(Role.USER)

                            .requestMatchers(GET, "api/reply/**").hasAnyRole(Role.USER, Role.ADMIN, Role.TEACHER)
                            .requestMatchers(POST, "api/reply/**").hasAnyRole(Role.USER, Role.TEACHER)

                            .requestMatchers(GET, "api/enrollments/**").hasAnyRole(Role.ADMIN, Role.USER, Role.TEACHER)
                            .requestMatchers(POST, "api/enrollments").hasAnyRole(Role.USER)

                            .requestMatchers(POST, "api/email").hasAnyRole(Role.USER, Role.ADMIN)

                            .requestMatchers(GET, "api/essays/**").hasAnyRole(Role.TEACHER, Role.ADMIN, Role.USER)
                            .requestMatchers(POST, "api/essays/**").hasAnyRole(Role.USER)
                            .requestMatchers(PUT, "api/essays/**").hasAnyRole(Role.USER)

                            .requestMatchers(GET, "api/lessons").hasAnyRole(Role.USER, Role.ADMIN, Role.TEACHER)
                            .requestMatchers(GET, "api/lessons/auth/**").hasAnyRole(Role.USER, Role.ADMIN, Role.TEACHER)
//                            .requestMatchers(GET, "api/lessons/count-by-course/").permitAll()
                            .requestMatchers(GET, "api/lessons/get-first-lesson/course/**",
                                    "api/lessons/count-by-course/").permitAll()

                            .requestMatchers(POST, "api/lessons/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(POST, "api/lessons/uploads/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(PUT, "api/lessons/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(PUT, "api/lessons/**/active").hasAnyRole(Role.ADMIN)
                            .requestMatchers(PUT, "api/lessons/update-video/").hasAnyRole(Role.ADMIN)
                            .requestMatchers(DELETE, "api/lessons/**").hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET, "api/like/**").hasAnyRole(Role.USER, Role.TEACHER)
                            .requestMatchers(POST, "api/like").hasAnyRole(Role.USER, Role.TEACHER)

                            .requestMatchers(GET, "api/login/auth/social-login").permitAll()
                            .requestMatchers(GET, "api/login/auth/social/callback").permitAll()

                            .requestMatchers(POST, "api/payment/update-payment").hasAnyRole(Role.USER)
                            .requestMatchers(GET, "api/payment/check-payment").hasAnyRole(Role.USER)

                            .requestMatchers(POST, "api/paypal/**").hasAnyRole(Role.USER)
                            .requestMatchers(GET, "api/paypal/**").hasAnyRole(Role.USER)

                            .requestMatchers(GET, "api/progress/user/").hasAnyRole(Role.ADMIN)
                            .requestMatchers(GET, "api/progress/get-progress/").hasAnyRole(Role.USER)
                            .requestMatchers(POST, "api/progress/**").hasAnyRole(Role.USER, Role.TEACHER, Role.ADMIN)
                            .requestMatchers(GET, "api/progress/check-progress/").hasAnyRole(Role.USER, Role.TEACHER)

                            .requestMatchers(GET, "api/questions/count/assignment/**").permitAll()
                            .requestMatchers(GET, "api/questions/assignment/").hasAnyRole(Role.TEACHER, Role.USER)
                            .requestMatchers(POST, "api/questions/**").hasAnyRole(Role.TEACHER, Role.ADMIN)
                            .requestMatchers(PUT, "api/questions/**").hasAnyRole(Role.TEACHER, Role.ADMIN)

                            .requestMatchers(GET, "api/roles/").hasAnyRole(Role.ADMIN)

                            .requestMatchers(POST, "api/receipts/create-payment").hasAnyRole(Role.USER)
                            .requestMatchers(GET, "api/receipts/get-all").hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET, "api/receipt_details").hasAnyRole(Role.ADMIN)

                            .requestMatchers(POST, "api/register/").hasAnyRole(Role.USER)
                            .requestMatchers(GET, "api/register/**").hasAnyRole(Role.ADMIN, Role.USER, Role.TEACHER)
                            .requestMatchers(PATCH, "api/register/update/**").hasAnyRole(Role.ADMIN)

                            .requestMatchers(POST, "api/score/**").hasAnyRole(Role.TEACHER, Role.ADMIN, Role.USER)
                            .requestMatchers(GET, "api/score/**").hasAnyRole(Role.USER, Role.TEACHER, Role.ADMIN)

                            .requestMatchers(POST, "api/spell-check").hasAnyRole(Role.USER)

                            .requestMatchers(GET, "api/token/get-list-tokens").hasAnyRole(Role.ADMIN, Role.TEACHER, Role.USER)
                            .requestMatchers(POST, "/api/token").permitAll()
                            .requestMatchers(DELETE, "api/token/userId").permitAll()
                            .requestMatchers(DELETE, "api/token/remove-token").hasAnyRole(Role.USER)

                            .requestMatchers(GET, "api/teachers/all").permitAll()
                            .requestMatchers(GET, "api/teachers/user/").permitAll()
                            .requestMatchers(GET, "api/teachers/{teacherId}/get-teacherId").permitAll()
                            .requestMatchers(GET, "api/teachers/get-teacher-by-userId/**").permitAll()
                            .requestMatchers(GET, "/api/teachers/{teacherId}").hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET, "/api/teacher/get-information/").hasAnyRole(Role.TEACHER, Role.ADMIN)

                            .requestMatchers(POST, "/api/teachers/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(PUT, "/api/teachers/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(DELETE, "/api/teachers/**").hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET, "api/users/all-users").hasAnyRole(Role.ADMIN)
                            .requestMatchers(GET, "api/users/auth/").permitAll()
                            .requestMatchers(GET, "api/users/get-users/").hasAnyRole(Role.ADMIN)
                            .requestMatchers(GET, "api/users/current-user").hasAnyRole(Role.ADMIN, Role.USER, Role.TEACHER)
                            .requestMatchers(GET, "/api/users/get-userId").permitAll()
                            .requestMatchers(GET, "/api/users/get-user/").permitAll()

                            .requestMatchers(POST, "api/users/**").hasAnyRole(Role.ADMIN)
                            .requestMatchers(PATCH, "api/users/update-active/").hasAnyRole(Role.ADMIN)
                            .requestMatchers(PUT, "api/users/update-user/").hasAnyRole(Role.ADMIN, Role.USER, Role.TEACHER)
                            .requestMatchers(DELETE, "api/users/**").hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET, "api/tags").hasAnyRole(Role.ADMIN, Role.TEACHER)
                            .requestMatchers(POST, "api/tags").hasAnyRole(Role.ADMIN)

                            .requestMatchers(PUT, "api/receipts/**").hasAnyRole(Role.USER)

                            .requestMatchers(POST, "api/views/").hasAnyRole(Role.USER)

                            .requestMatchers(PUT, "api/videos/update/").hasAnyRole(Role.ADMIN)
                            .requestMatchers(GET, "api/videos/count/lesson/").permitAll()
                            .requestMatchers(GET, "api/videos/count/course/").permitAll()


                            .requestMatchers(GET, "api//**").hasAnyRole(Role.USER, Role.ADMIN, Role.TEACHER)
                            .requestMatchers(POST, "api/video-completed/").hasAnyRole(Role.USER)

                            .anyRequest().authenticated();
                });
        return http.build();
    }
}
