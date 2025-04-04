package com.htt.elearning.view.repository;

import com.htt.elearning.view.pojo.View;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepository extends JpaRepository<View, Long> {
    View findByUserIdAndCourseId(Long userId, Long courseId);
}
