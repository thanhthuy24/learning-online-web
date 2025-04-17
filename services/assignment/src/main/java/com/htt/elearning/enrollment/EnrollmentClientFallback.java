package com.htt.elearning.enrollment;

import com.htt.elearning.user.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class EnrollmentClientFallback implements EnrollmentClient {
    @Override
    public Boolean checkEnrollment(Long userId, Long courseId, String token) {
        log.warn("Fallback: Cannot check enrollment with userId {}", userId);
        throw new RuntimeException("Simulated fallback failure");
    }

    @Override
    public List<UserResponse> getUsersByCourseIdClient(Long courseId, String token) {
        log.warn("Fallback: Cannot get courseId list for ids {}", courseId);
        return Collections.emptyList(); // or throw custom exception
    }
}
