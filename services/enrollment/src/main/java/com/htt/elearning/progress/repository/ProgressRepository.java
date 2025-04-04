package com.htt.elearning.progress.repository;

import com.htt.elearning.progress.pojo.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByCourseIdAndUserId(Long courseId, Long userId);
    List<Progress> findProgressByUserId(Long userId);
    Progress findProgressByCourseIdAndUserId(Long courseId, Long userId);
}
