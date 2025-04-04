package com.htt.elearning.lesson;


import com.htt.elearning.lesson.response.LessonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "course-service",
        contextId = "courseServiceV2",
        url = "${application.config.lesson-url}"
)
public interface LessonClient {
    @GetMapping("/get-lesson/{lessonId}")
    LessonResponse getLessonById(@PathVariable("lessonId") Long lessonId);

    @GetMapping("/get-list-lessons/{courseId}")
    List<LessonResponse> getListLessonsByCourseIdClient(
            @PathVariable Long courseId
    );
}
