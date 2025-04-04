package com.htt.elearning.assignment.repository;

import com.htt.elearning.assignment.pojo.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Page<Assignment> findAll(Pageable pageable);
    List<Assignment> findByLessonId(Long lessonId);
    List<Assignment> findByCourseId(Long courseId);
    Assignment findAssignmentById(Long assignmentId);
}
