package com.htt.elearning.teacher.controller;

import com.htt.elearning.teacher.pojo.Teacher;
import com.htt.elearning.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/get-information/{teacherId}")
    public ResponseEntity<Teacher> getInformationTeacher(
            @PathVariable Long teacherId
    ){
        Teacher teacher = teacherService.getInformation(teacherId);
        return ResponseEntity.ok(teacher);
    }
}
