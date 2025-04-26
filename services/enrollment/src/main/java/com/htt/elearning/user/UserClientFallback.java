package com.htt.elearning.user;
import com.htt.elearning.user.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class UserClientFallback implements UserClient {
    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public Long getUserIdByUsername(String token) {
        return 0L;
    }

    @Override
    public Long getRoleIdClient(String token) {
        return 0L;
    }

    @Override
    public ResponseEntity<UserResponse> getCurrentUser() {
        return null;
    }

    @Override
    public UserResponse getUserByIdClient(Long userId, String token) {
        log.warn("Fallback: Cannot get teacher info for id {}", userId);
        return null;
    }

    @Override
    public List<UserResponse> getUsersByIdsClient(List<Long> userIds, String token) {
        log.warn("Fallback: Cannot get teacher list for ids {}", userIds);
        return Collections.emptyList();
    }
}
