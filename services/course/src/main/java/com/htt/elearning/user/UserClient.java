package com.htt.elearning.user;

import com.htt.elearning.config.FeignConfig;
import com.htt.elearning.user.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "user-service",
        contextId = "userServiceV1",  // Thêm contextId duy nhất
        url = "${application.config.user-url}",
        configuration = FeignConfig.class
)
public interface UserClient {
    @GetMapping("/get-username")
    String getUsername();

    @GetMapping("/get-userId")
    Long getUserIdByUsernameClient(@RequestHeader("Authorization") String token);

    @GetMapping("/get-role-id")
    Long getRoleIdClient(@RequestHeader("Authorization") String token);

    @GetMapping("/get-current-user")
    ResponseEntity<UserResponse> getCurrentUser();

    @GetMapping("/get-user/{userId}")
    UserResponse getUserByIdClient(
            @PathVariable("userId") Long userId,
            @RequestHeader("Authorization") String token
    );
}
