package com.htt.elearning.assignment.service;
import com.htt.elearning.assignment.dto.AssignmentDTO;
import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.assignment.response.AssignmentResponse;
import com.htt.elearning.exceptions.DataNotFoundException;
import org.springframework.data.domain.*;

import java.util.List;
public interface AssignmentService {
    Page<AssignmentResponse> getAllAssignment(PageRequest pageRequest);
    Assignment getAssignmentById(Long id);
    Assignment createAssignment(AssignmentDTO assignmentDTO) throws DataNotFoundException;

    List<Assignment> getAssignmentByLessonId(Long lessonId);
    List<Assignment> getAssignmentByCourseId(Long courseId);

    Assignment updateAssignment(Long assignmentId, AssignmentDTO assignmentDTO) throws DataNotFoundException;
    void deleteAssignment(Long id);
}
