package com.htt.elearning.userassignmentdone.repository;

import com.htt.elearning.userassignmentdone.pojo.Userassignmentdone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentDoneRepository extends JpaRepository<Userassignmentdone, Long> {
    Userassignmentdone findByUserIdAndAssignmentId(Long userId, Long assignmentId);
    Long countByAssignmentId(Long assignmentId);
}
