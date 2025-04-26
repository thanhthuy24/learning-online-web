package com.htt.elearning.user;

import com.htt.elearning.config.FeignConfig;
import com.htt.elearning.user.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "user-service",
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

    @GetMapping("/get-users-by-ids")
    List<UserResponse> getUsersByIdsClient(
            @RequestParam("userIds") List<Long> userIds,
            @RequestHeader("Authorization") String token
    );
}
