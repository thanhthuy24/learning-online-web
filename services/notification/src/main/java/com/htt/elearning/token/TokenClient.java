package com.htt.elearning.token;

import com.htt.elearning.token.response.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "user-service",
        contextId = "userServiceV2",  // Thêm contextId duy nhất
        url = "${application.config.token-url}"
)
public interface TokenClient {
    @GetMapping("/get-list-tokens")
    List<TokenResponse> getListTokens(
            @RequestParam List<Long> userIds,
            @RequestHeader("Authorization") String token
    );
}
