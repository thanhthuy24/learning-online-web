package com.htt.elearning.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service",
                        r -> r.path(
                                "/api/users/**",
                                "/api/roles/**",
                                "/api/teachers/**",
                                "/api/token/**",
                                "/api/teacher/**")
                        .uri("lb://USER-SERVICE"))
                .route("courses-service",
                        r -> r.path(
                                "/api/courses/**",
                                "/api/category/**",
                                "/api/tags/**",
                                "/api/lessons/**",
                                "/api/videos/**",
                                "/api/video-completed/**",
                                "/api/ai-recommend/**")
                        .uri("lb://COURSES-SERVICE"))
                .route("assignment-service",
                        r -> r.path(
                                "/api/assignments/**",
                                "/api/answer-choices/**",
                                "/api/choices/**",
                                "/api/essays/**",
                                "/api/questions/**",
                                "/api/score/**",
                                "/api/assignment-done/**"
                                )
                        .uri("lb://ASSIGNMENT-SERVICE"))
                .route("cloudinary-service",
                        r->r.path(
                                "/api/cloudinary/**"
                        ).uri("lb://CLOUDINARY-SERVICE"))
                .route("enrollment-service",
                        r -> r.path(
                                "/api/enrollments/**",
                                "/api/certificate/**",
                                "/api/comments/**",
                                "/api/reply/**",
                                "/api/progress/**",
                                "/api/rating/**"
                        ).uri("lb://ENROLLMENT-SERVICE"))
                .route("notification-service",
                        r -> r.path(
                                "/api/get-notification/**",
                                "/api/notifications/**",
                                "/api/email/**"
                        ).uri("lb://NOTIFICATION-SERVICE"))
                .route("payment-service",
                        r -> r.path(
                                "/api/receipts/**",
                                "/api/receipt_details/**",
                                "/api/payment/**",
                                "/api/paypal/**"
                        ).uri("lb://PAYMENT-SERVICE"))
                .route("register-service",
                        r->r.path(
                                "/api/register/**"
                        ).uri("lb://REGISTER-SERVICE"))
                .route("view-service",
                        r->r.path(
                                "/api/views/**"
                        ).uri("lb://VIEW-SERVICE"))
                .build();
    }
}
