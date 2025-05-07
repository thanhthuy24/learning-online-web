package com.htt.elearning.interactions.repository;

import com.htt.elearning.interactions.pojo.Interaction;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    Interaction findByUserIdAndCourseId(Long userId, Long courseId);

    @Query("""
        SELECT i
        FROM Interaction i
        WHERE i.userId = :userId AND i.courseId = :courseId 
        """)
    Interaction getInteractionByUserIdAndCourseId(Long userId, Long courseId);
}
