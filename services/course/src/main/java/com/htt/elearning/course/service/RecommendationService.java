package com.htt.elearning.course.service;

import com.htt.elearning.course.dto.CourseDTO;
import com.htt.elearning.course.pojo.Course;

import java.util.List;

public interface RecommendationService {
    List<CourseDTO> getRecommendedCourses(Long userId);
    List<Course> getRecommendedCourses2(Long userId);
}
