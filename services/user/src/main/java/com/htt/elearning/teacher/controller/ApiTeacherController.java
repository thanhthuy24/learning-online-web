package com.htt.elearning.teacher.controller;

import com.htt.elearning.teacher.dto.TeacherDTO;
import com.htt.elearning.teacher.pojo.Teacher;
import com.htt.elearning.teacher.response.TeacherListResponse;
import com.htt.elearning.teacher.response.TeacherResponse;
import com.htt.elearning.teacher.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/teachers")
public class ApiTeacherController {
    private final TeacherService teacherService;
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TeacherListResponse> getAllTeachers(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        // tao pageable tu thong tin page va limit
        Pageable pageRequest = PageRequest.of(page, limit);
        Page<Teacher> teacherPage = teacherService.getAllTeachers(pageRequest);

        // lay tong so trang
        int totalPage = teacherPage.getTotalPages();
        List<Teacher> teachers = teacherPage.getContent();
        return ResponseEntity.ok(TeacherListResponse.builder()
                .teachers(teachers)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(teacherService.getAll());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createTeacher(
            @Valid
            @RequestBody TeacherDTO teacherDTO,
            BindingResult rs) {
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        teacherService.createTeacher(teacherDTO);
        return ResponseEntity.ok(teacherDTO);
    }

    @GetMapping("/{teacherId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getTeacherById(
            @PathVariable("teacherId") Long teacherId
    ){
        Teacher teacher = teacherService.getTeacherById(teacherId);
        return ResponseEntity.ok(teacher);
    }

    @PutMapping("/{teacherId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateTeacher(
            @PathVariable("teacherId") Long teacherId,
            @Valid @RequestBody TeacherDTO teacherDTO
    ){
        teacherService.updateTeacher(teacherId, teacherDTO);
        return  ResponseEntity.ok(teacherDTO);
    }

    @DeleteMapping("/{teacherId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteTeacher(
            @PathVariable("teacherId") Long teacherId

    ){
        teacherService.deleteTeacher(teacherId);
        return  ResponseEntity.ok("delete teacher successfully!!");
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getTeacherByUserId(
            @PathVariable Long userId
    ){
        return ResponseEntity.ok(teacherService.getTeacherByUserId(userId));
    }

//    teacher - client

    @GetMapping("/{teacherId}/get-teacherId")
    @ResponseStatus(HttpStatus.OK)
    public TeacherResponse getTeacherClientById(
            @PathVariable("teacherId") Long teacherId
    ){
        return teacherService.getTeacherByIdClient(teacherId);
    }

    @GetMapping("/get-teacher-by-userId/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public TeacherResponse getTeacherByUserIdClient(
            @PathVariable("userId") Long userId

    ){
        return teacherService.getTeacherByUserIdClient(userId);
    }
}
