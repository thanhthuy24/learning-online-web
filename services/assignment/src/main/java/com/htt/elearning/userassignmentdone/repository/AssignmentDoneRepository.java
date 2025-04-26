package com.htt.elearning.userassignmentdone.repository;

import com.htt.elearning.userassignmentdone.pojo.Userassignmentdone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentDoneRepository extends JpaRepository<Userassignmentdone, Long> {
    Userassignmentdone findByAssignmentId(Long assignmentId);
    Long countByAssignmentId(Long assignmentId);
}
