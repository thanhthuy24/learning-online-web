package com.htt.elearning.user;

import com.htt.elearning.user.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "${application.config.user-url}"
)
public interface UserClient {
    @GetMapping("/get-username")
    String getUsername();

    @GetMapping("/get-userId")
    Long getUserIdByUsername();

    @GetMapping("/get-role-id")
    Long getRoleIdClient();

    @GetMapping("/get-current-user")
    ResponseEntity<UserResponse> getCurrentUser();

    @GetMapping("/get-user/{userId}")
    UserResponse getUserByIdClient(
            @PathVariable("userId") Long userId
    );

    @GetMapping("/update-role-teacher/{userId}")
    UserResponse updateRoleTeacher(
            @PathVariable("userId") Long userId
    );
}
