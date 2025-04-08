package com.htt.elearning.course.service;

import com.htt.elearning.course.dto.CourseDTO;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.course.response.CourseResponseRedis;
import org.springframework.data.domain.*;

import java.util.List;

public interface CourseService {
    List<Course> searchCourses(String keyword);
    Course createCourse(CourseDTO courseDTO);
    Course getCourseById(Long id);
    Page<Course> getAllCourses(Pageable pageable);

    Page<Course> getCoursesByTeacherId(Long teacherId, Pageable pageable);

    Course updateCourse(Long id, CourseDTO courseDTO);
    void deleteCourse(Long id);
    boolean existByName(String name);
    List<Course> getAllCourseName();
    List<Course> getCoursesByCategoryId(Long categoryId);
    Page<Course> getCoursesByCategoryIdPage(Pageable pageable, Long categoryId);
    Page<Course> getCoursesByPrice(Float minPrice, Float maxPrice, Pageable pageable);

//    course - client
    CourseResponse getCourseByIdClient(Long courseId);
    List<Long> searchCourseIdsByNameClient(String keyword);

    Page<CourseResponseRedis> getAllCoursesRedisClient(Pageable pageable, String keyword, Long categoryId);
}
