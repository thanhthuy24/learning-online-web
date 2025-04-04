package com.htt.elearning.score.repository;

import com.htt.elearning.score.pojo.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findById(Long id);
    Optional<Score> findByUserIdAndAssignmentId(Long userId, Long assignmentId);
    List<Score> findByAssignmentId(Long assignmentId);
}
