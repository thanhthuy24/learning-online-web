package com.htt.elearning.course.service;

import com.htt.elearning.course.dto.CourseDTO;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final CourseRepository courseRepository;
    private final RestTemplate restTemplate;

    @Value("${python.api.url}")
    private String pythonApiUrl;
//    private final String FLASK_API_URL = "https://1583-35-243-254-32.ngrok-free.app/recommend?user_id=";

    @Override
    public List<CourseDTO> getRecommendedCourses(Long userId) {
        List<Long> recommendedCourseId = restTemplate.getForObject(pythonApiUrl + "/recommend?user_id=" + userId, List.class);
        List<Course> courseList = courseRepository.findCoursesByIds(recommendedCourseId);
        return courseList.stream()
                .map(this::convertToCourseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> getRecommendedCourses2(Long userId) {
        List<Long> recommendedCourseId = restTemplate.getForObject(pythonApiUrl + "/recommend?user_id=" + userId, List.class);
        List<Course> courseList = courseRepository.findCoursesByIds(recommendedCourseId);
        return courseList;
    }

    private CourseDTO convertToCourseDTO(Course course) {
        return CourseDTO.builder()
                .name(course.getName())
                .description(course.getDescription())
                .image(course.getImage())
                .price(course.getPrice())
                .discount(course.getDiscount())
                .categoryId(course.getCategory().getId())
                .tagId(course.getTag().getId())
                .teacherId(course.getTeacherId())
                .build();
    }
}
