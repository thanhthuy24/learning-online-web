package com.htt.elearning.view.service;

import com.htt.elearning.course.CourseClient;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.view.pojo.View;
import com.htt.elearning.view.repository.ViewRepository;
import com.htt.elearning.view.response.ViewResponse;
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

    @Override
    public ViewResponse createView(Long courseId) {
        CourseResponse existingCourse = courseClient.getCourseByIdClient(courseId);

        Long userId = userClient.getUserIdByUsername();

        View existingView = viewRepository.findByUserIdAndCourseId(userId, courseId);
        if (existingView != null) {
            existingView.setCourseId(existingCourse.getId());
            existingView.setUserId(userId);
            existingView.setViewCount(existingView.getViewCount() + 1);

            viewRepository.save(existingView);

            return modelMapper.map(existingView, ViewResponse.class);
        }

        View view = View.builder()
                .createdDate(new Date())
                .courseId(existingCourse.getId())
                .userId(userId)
                .viewCount(1L)
                .build();

        viewRepository.save(view);
        return modelMapper.map(view, ViewResponse.class);
    }
}
