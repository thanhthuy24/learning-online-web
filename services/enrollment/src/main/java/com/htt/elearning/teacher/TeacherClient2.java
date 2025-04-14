package com.htt.elearning.teacher;

import com.htt.elearning.teacher.response.TeacherResponseClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "user-service",
        contextId = "userServiceV2",  // Thêm contextId duy nhất
        url = "http://localhost:8090/api/teacher"
)
public interface TeacherClient2 {
    @GetMapping("/get-information")
    List<TeacherResponseClient> getInformationTeacher(
            @RequestParam("teacherIds") List<Long> teacherIds
    );
}
