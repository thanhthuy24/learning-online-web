package com.htt.elearning.assignment.controller;

import com.htt.elearning.assignment.dto.AssignmentDTO;
import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.assignment.response.AssignmentListResponse;
import com.htt.elearning.assignment.response.AssignmentResponse;
import com.htt.elearning.assignment.service.AssignmentService;
import com.htt.elearning.exceptions.DataNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class ApiAssignmentController {
    private final AssignmentService assignmentService;
//    private final NotificationService notificationService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AssignmentListResponse> getAllAssignments(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("createdDate").descending());
        Page<AssignmentResponse> assignmentPage = assignmentService.getAllAssignment(pageRequest);

        // lay tong so trang
        int totalPage = assignmentPage.getTotalPages();
        List<AssignmentResponse> assignments = assignmentPage.getContent();
        return ResponseEntity.ok(AssignmentListResponse.builder()
                .assignments(assignments)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping("/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Assignment> getAssignmentById(
            @PathVariable("assignmentId") Long assignmentId
    ) {
        Assignment assignmentById = assignmentService.getAssignmentById(assignmentId);
        return ResponseEntity.ok(assignmentById);
    }

    @GetMapping("/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAssignmentsByCourseId(
            @PathVariable(value = "courseId") Long courseId
    ){
        List<Assignment> assignmentList = assignmentService.getAssignmentByCourseId(courseId);
        return ResponseEntity.ok(assignmentList);
    }

    @GetMapping("/lesson/{lessonId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAssignmentsByLessonId(
            @PathVariable Long lessonId
    ){
        List<Assignment> assignmentList = assignmentService.getAssignmentByLessonId(lessonId);
        return ResponseEntity.ok(assignmentList);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createAssignment(
            @Valid
            @ModelAttribute AssignmentDTO assignmentDTO,
            BindingResult rs) throws Exception {
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Assignment asignment = assignmentService.createAssignment(assignmentDTO);
//        notificationService.sendNotificationToEnrolledUsers(
//                assignmentDTO.getCourseId(),
//                "Bài tập mới!",
//                "Khóa học của bạn có bài tập mới vừa được tạo: " + assignmentDTO.getName()
//        );
        return ResponseEntity.ok(asignment);
    }

    @PutMapping("/{assignmentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> updateAssignment(
            @Valid
            @ModelAttribute AssignmentDTO assignmentDTO,
            @PathVariable("assignmentId") Long assignmentId,
            BindingResult rs
    )throws DataNotFoundException {
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        assignmentService.updateAssignment(assignmentId, assignmentDTO);
        return ResponseEntity.ok(assignmentDTO);
    }

}
