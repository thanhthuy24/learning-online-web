package com.htt.elearning.view.service;

import com.htt.elearning.course.CourseClient;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.view.pojo.View;
import com.htt.elearning.view.repository.ViewRepository;
import com.htt.elearning.view.response.ViewResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ViewServiceImpl implements ViewService {
    private final ViewRepository viewRepository;
    private final UserClient userClient;
    private final CourseClient courseClient;
    private final ModelMapper modelMapper;
    private final HttpServletRequest request;

    @Override
    public ViewResponse createView(Long courseId) {
        String token = request.getHeader("Authorization");
        CourseResponse existingCourse = courseClient.getCourseByIdClient(courseId, token);

        Long userId = userClient.getUserIdByUsername(token);

        View existingView = viewRepository.findByUserIdAndCourseId(userId, courseId);
        if (existingView != null) {
            existingView.setCourseId(existingCourse.getId());
            existingView.setUserId(userId);
            existingView.setViewCount(existingView.getViewCount() + 1);

            viewRepository.save(existingView);

            return ViewResponse.fromView(existingView);
        }

        View view = View.builder()
                .createdDate(new Date())
                .courseId(existingCourse.getId())
                .userId(userId)
                .viewCount(1L)
                .build();

        viewRepository.save(view);
        return ViewResponse.fromView(view);
    }
}
