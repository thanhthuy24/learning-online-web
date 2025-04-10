package com.htt.elearning.teacher;

import com.htt.elearning.teacher.response.TeacherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "user-service",
        contextId = "userServiceV2",  // Thêm contextId duy nhất
        url = "http://localhost:8090/api/teachers"
)
public interface TeacherClient {
    @GetMapping("/{teacherId}/get-teacherId")
    TeacherResponse getTeacherById(
            @PathVariable("teacherId") Long teacherId,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/get-teacher-by-userId/{userId}")
    TeacherResponse getTeacherByUserIdClient(
            @PathVariable("userId") Long userId,
            @RequestHeader("Authorization") String token
    );
}
