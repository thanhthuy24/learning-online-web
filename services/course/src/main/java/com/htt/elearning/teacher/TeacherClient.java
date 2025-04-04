package com.htt.elearning.teacher;

import com.htt.elearning.teacher.response.TeacherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        contextId = "userServiceV2",  // Thêm contextId duy nhất
        url = "http://localhost:8080/api/teachers"
)
public interface TeacherClient {
    @GetMapping("/{teacherId}/get-teacherId")
    TeacherResponse getTeacherById(@PathVariable("teacherId") Long teacherId);
}
