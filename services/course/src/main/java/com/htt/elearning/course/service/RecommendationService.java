package com.htt.elearning.course.service;

import com.htt.elearning.course.dto.CourseDTO;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.response.TestCourseResponse;

import java.util.List;

public interface RecommendationService {
    List<CourseDTO> getRecommendedCourses(Long userId);
    List<TestCourseResponse> getRecommendedCourses2(Long userId);
}
