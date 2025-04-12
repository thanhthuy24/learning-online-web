package com.htt.elearning.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.util.AntPathMatcher;


@RequiredArgsConstructor
@Component
public class AuthenticationHeaderFilter implements GlobalFilter {
    private final OpenApiConfig openApiConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();
        AntPathMatcher matcher = new AntPathMatcher();

        // Nếu là GET và match trong open-paths-get
        boolean isOpenGet = method == HttpMethod.GET &&
                openApiConfig.getOpenPathsGet().stream().anyMatch(pattern -> matcher.match(pattern, path));

        // Nếu match trong open-paths-all (mọi method đều được)
        boolean isOpenPost = method == HttpMethod.POST &&
                openApiConfig.openPathsPost().stream().anyMatch(pattern -> matcher.match(pattern, path));

        if (isOpenGet || isOpenPost) {
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
}
