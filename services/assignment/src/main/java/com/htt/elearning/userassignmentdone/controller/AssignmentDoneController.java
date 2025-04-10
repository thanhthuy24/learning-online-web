package com.htt.elearning.userassignmentdone.controller;

import com.htt.elearning.userassignmentdone.dto.AssignmentDoneDTO;
import com.htt.elearning.userassignmentdone.pojo.Userassignmentdone;
import com.htt.elearning.userassignmentdone.service.AssignmentDoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/assignment-done")
public class AssignmentDoneController {
    private final AssignmentDoneService assignmentDoneService;

    @GetMapping("/assignment/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Userassignmentdone> getAssignmentDone(
            @PathVariable Long assignmentId) {
        Userassignmentdone userassignmentdone = assignmentDoneService.getAssignmentDone(assignmentId);
        return ResponseEntity.ok(userassignmentdone);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Userassignmentdone> createAssignmentDone(
            @RequestBody AssignmentDoneDTO assignmentDoneDTO,
            BindingResult rs
    ) {
        Userassignmentdone newAssignmentDone = assignmentDoneService.createAssignmentDone(assignmentDoneDTO);
        return ResponseEntity.ok(newAssignmentDone);
    }

    @GetMapping("/assignment/{assignmentId}/count")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> getCountAssignmentDone(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentDoneService.getCountByAssignmentId(assignmentId));
    }
}
