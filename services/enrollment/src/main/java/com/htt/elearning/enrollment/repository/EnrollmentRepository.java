package com.htt.elearning.enrollment.repository;

import com.htt.elearning.enrollment.pojo.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);
    List<Enrollment> findByUserId(Long userId);
    long countEnrollmentByCourseId(Long courseId);
    Long countEnrollmentByUserId(Long userId);
    List<Enrollment> findAllByCourseId(Long courseId);

    @Query("SELECT e.userId FROM Enrollment e where e.courseId = :courseId")
    List<Long> findAllEnrollmentByCourseId(@Param("courseId") Long courseId);

//    @Query("SELECT e.user FROM Enrollment e WHERE e.courseId = :courseId")
//    List<User> findUsersByCourseId(@Param("courseId") Long courseId);
}
