package com.htt.elearning.course.repository;

import com.htt.elearning.course.pojo.Course;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchByKeyword(@Param("keyword") String keyword);
    boolean existsByName(String name);
    Page<Course> findAll(Pageable pageable); //phân trang

    Page<Course> findByTeacherId(Long teacherId, Pageable pageable);

    Course getCourseById(Long id);
    List<Course> findByCategoryId(Long categoryId);

    Page<Course> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE " +
            "(:minPrice IS NULL OR c.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR c.price <= :maxPrice)")
    Page<Course>  getCoursesByPrice(@Param("minPrice") Float minPrice, @Param("maxPrice") Float maxPrice, Pageable pageable);
//    Page<Course> getCoursesByTeacherId(Long teacherId, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.id IN :ids")
    List<Course> findCoursesByIds(@Param("ids") List<Long> ids);
}
