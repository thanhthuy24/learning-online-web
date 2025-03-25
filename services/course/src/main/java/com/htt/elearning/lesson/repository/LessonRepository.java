package com.htt.elearning.lesson.repository;

import com.htt.elearning.lesson.pojo.Lesson;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    boolean existsByName(String name);
    Page<Lesson> findAll(Pageable pageable);
    @Query("SELECT l FROM Lesson l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(l.course.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Lesson> searchLessonsAll(@Param("keyword") String keyword, Pageable pageable);
    List<Lesson> findByCourseId(Long courseId);
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.course.id = :courseId")
    long countLessonsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId ORDER BY l.createdDate LIMIT 1")
    Lesson findFirstLesson(@Param("courseId") Long courseId);
}
