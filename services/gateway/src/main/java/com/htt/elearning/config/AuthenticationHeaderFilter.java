package com.htt.elearning.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class AuthenticationHeaderFilter implements GlobalFilter {
    private final OpenApiConfig openApiConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Nếu là public API, không cần token
        if (openApiConfig.getOpenPaths().stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // Kiểm tra token
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .build();
            return chain.filter(exchange.mutate().request(request).build());
        }

        // Nếu không có token hoặc sai format
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        if (token != null && token.startsWith("Bearer ")) {
//            ServerHttpRequest request = exchange.getRequest()
//                    .mutate()
//                    .header(HttpHeaders.AUTHORIZATION, token)
//                    .build();
//            return chain.filter(exchange.mutate().request(request).build());
//        }
//        System.out.println("Token: " + token);
//        return chain.filter(exchange);
//    }
}
