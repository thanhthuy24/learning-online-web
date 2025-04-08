package com.htt.elearning.course.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.course.response.CourseResponseRedis;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CourseRedisService {
    void clear();

    List<CourseResponseRedis> getAllCourses(
            PageRequest pageRequest,
            Long categoryId,
            String keyword
    ) throws JsonProcessingException;

    void saveAllCourses(List<CourseResponseRedis> courses,
                        PageRequest pageRequest,
                        Long categoryId,
                        String keyword) throws JsonProcessingException;
}
