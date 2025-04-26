package com.htt.elearning.essay.controller;

import com.htt.elearning.assignment.service.AssignmentService;
import com.htt.elearning.essay.dto.EssayDTO;
import com.htt.elearning.essay.pojo.Essay;
import com.htt.elearning.essay.response.EssayListResponse;
import com.htt.elearning.essay.response.EssayListResponseClient;
import com.htt.elearning.essay.response.EssayResponseClient;
import com.htt.elearning.essay.service.EssayService;
import com.htt.elearning.exceptions.DataNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/essays")
public class EssayController {
    private final EssayService essayService;
    private final AssignmentService assignmentService;

    @GetMapping("/assignment/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getEssayByAssignmentId(
            @PathVariable("assignmentId") Long assignmentId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {

        if (page < 0 || limit <= 0) {
            return ResponseEntity.badRequest().body("Page and limit must be positive numbers.");
        }

        if (assignmentService.getAssignmentById(assignmentId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment not found.");
        }

        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("createdDate").descending());
        Page<EssayResponseClient> essayPage = essayService.getEssaysByAssignment(assignmentId, pageRequest);

        int totalPages = essayPage.getTotalPages();
        List<EssayResponseClient> essays = essayPage.getContent();
        return ResponseEntity.ok(EssayListResponseClient.builder()
                .essays(essays)
                .totalPages(totalPages)
                .build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createEssay(
            @Valid @RequestBody List<EssayDTO> essayDTOs,
            BindingResult rs
    ) throws DataNotFoundException {
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        for (EssayDTO essayDTO : essayDTOs) {
            essayService.createEssay(essayDTO);
        }
        return ResponseEntity.ok(essayDTOs);
    }

    @PostMapping("/create-an-essay")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createAnEssay(
            @Valid @RequestBody EssayDTO essayDTO,
            BindingResult rs
    ) throws DataNotFoundException {
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Essay essay = essayService.createEssay(essayDTO);
        return ResponseEntity.ok(essay);
    }

    @PutMapping("/{essayId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateEssay(
            @Valid
            @PathVariable Long essayId,
            @RequestBody EssayDTO essayDTO,
            BindingResult rs
    )throws DataNotFoundException{
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        essayService.updateEssay(essayId, essayDTO);
        return ResponseEntity.ok(essayDTO);
    }

//    @GetMapping("/{assignmentId}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<?> getEssayById(
//            @PathVariable Long assignmentId
//    ){
//        return ResponseEntity.ok(essayService.getEssayById(assignmentId));
//    }
}
