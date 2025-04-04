package com.htt.elearning.answerchoice.repository;

import com.htt.elearning.answerchoice.pojo.Answerchoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface AnswerChoiceRepository extends JpaRepository<Answerchoice, Long> {
    Optional<Answerchoice> findById(Long id);
    List<Answerchoice> findByAssignmentIdAndUserId(Long assignmentId, Long userId);
    Optional<Answerchoice> findByAssignmentIdAndQuestionIdAndUserId
            (Long assignmentId, Long questionId, Long userId);
}
