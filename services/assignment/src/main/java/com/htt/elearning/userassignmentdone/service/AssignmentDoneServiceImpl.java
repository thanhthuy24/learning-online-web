package com.htt.elearning.userassignmentdone.service;
import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.assignment.repository.AssignmentRepository;
import com.htt.elearning.enrollment.EnrollmentClient;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.userassignmentdone.dto.AssignmentDoneDTO;
import com.htt.elearning.userassignmentdone.pojo.Userassignmentdone;
import com.htt.elearning.userassignmentdone.repository.AssignmentDoneRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentDoneServiceImpl implements AssignmentDoneService {
    private final AssignmentDoneRepository assignmentDoneRepository;
    private final UserClient userClient;
    private final AssignmentRepository assignmentRepository;
    private final EnrollmentClient enrollmentClient;
    private final HttpServletRequest request;

    @Override
    public Userassignmentdone createAssignmentDone(AssignmentDoneDTO assignmentDoneDTO){
        Assignment existingAssignment = assignmentRepository.findById(assignmentDoneDTO.getAssignmentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find Assignment with id "
                        + assignmentDoneDTO.getAssignmentId()));

        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);
        Boolean checkEnrollment = enrollmentClient.checkEnrollment(userId, existingAssignment.getCourseId(), token);
        if (checkEnrollment == null || !checkEnrollment) {
            new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This course isn't enrolled in your list! Please enroll before participating in this course!!");
        }

        Userassignmentdone newAssignmentDone = Userassignmentdone.builder()
                .assignment(existingAssignment)
                .userId(userId)
                .createdDate(new Date())
                .build();

        assignmentDoneRepository.save(newAssignmentDone);
        return newAssignmentDone;
    }

    @Override
    public Userassignmentdone getAssignmentDone(Long assignmentId) {
        Assignment existingAssignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find Assignment with id "
                        + assignmentId));
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        Userassignmentdone assignmentDone = assignmentDoneRepository.findByAssignmentId(assignmentId);
        if (assignmentDone == null) {
            return null;
        }
        return assignmentDone;
    }

    @Override
    public Long getCountByAssignmentId (Long assignmentId) {
        Assignment existingAssignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find Assignment with id "
                        + assignmentId));
        Long count = assignmentDoneRepository.countByAssignmentId(existingAssignment.getId());
        if (count == 0) {
            return 0L;
        } return count;
    }
}
