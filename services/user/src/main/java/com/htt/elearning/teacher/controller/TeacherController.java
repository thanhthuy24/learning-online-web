package com.htt.elearning.teacher.controller;

import com.htt.elearning.teacher.pojo.Teacher;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import com.htt.elearning.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/get-information")
    public List<TeacherResponseClient> getInformationTeacher(
            @RequestParam("teacherIds") List<Long> teacherIds
    ){
        return teacherService.getTeacherResponseClient(teacherIds);
    }

//    get a teacher's information
    @GetMapping("/get-information-teacher")
    public TeacherResponseClient getInformationOneTeacher(
            @RequestParam("teacherId") Long teacherId
    ){
        return teacherService.getOneTeacherResponseClient(teacherId);
    }

}
