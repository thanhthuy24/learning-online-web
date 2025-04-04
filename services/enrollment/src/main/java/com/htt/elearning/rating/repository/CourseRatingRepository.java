package com.htt.elearning.rating.repository;

import com.htt.elearning.rating.pojo.Courserating;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRatingRepository extends JpaRepository<Courserating, Long> {
    Optional<Courserating> findByCourseIdAndUserId(Long courseId, Long userId);
    Page<Courserating> findByCourseId(Long courseId, PageRequest pageRequest);
    List<Courserating> findByCourseId(Long courseId);
    Long countByCourseId(Long courseId);
    Long countByRating(Long rating);
    Long countByCourseIdAndRating(Long courseId, Long rating);

    @Query("SELECT r FROM Courserating r WHERE " +
            "LOWER(r.sentiment) LIKE LOWER(CONCAT('%', :keyword, '%')) AND " +
            "r.courseId = :courseId")
    Page<Courserating> findByKeyword(@Param("courseId") Long courseId,
                                     @Param("keyword") String keyword,
                                     PageRequest pageRequest);

    @Query("SELECT r FROM Courserating r WHERE " +
            "r.rating = :rating AND " +
            "r.courseId = :courseId")
    Page<Courserating> findByRating(@Param("courseId") Long courseId,
                                    @Param("rating") Long rating,
                                    PageRequest pageRequest);
}
