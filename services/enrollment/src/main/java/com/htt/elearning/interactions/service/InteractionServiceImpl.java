package com.htt.elearning.interactions.service;

import com.htt.elearning.course.CourseClient;
import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.enrollment.repository.EnrollmentRepository;
import com.htt.elearning.enrollment.service.EnrollmentService;
import com.htt.elearning.interactions.dto.InteractionDTO;
import com.htt.elearning.interactions.pojo.Interaction;
import com.htt.elearning.interactions.repository.InteractionRepository;
import com.htt.elearning.interactions.response.InteractionResponse;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {
    private final InteractionRepository interactionRepository;
    private final CourseClient courseClient;
    private final UserClient userClient;
    private final HttpServletRequest request;

    @Override
    public InteractionResponse createInteraction(Long courseId) {
        TestCourseResponse existingCourse = courseClient.getFullCourseResponse(courseId);
        if (existingCourse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsername(token);

        UserResponse userResponse = userClient.getUserByIdClient(userId, token);

//        Boolean checkEnrollment = enrollmentService.checkEnrolled(userId, courseId);

        Interaction existingInteraction = interactionRepository.findByUserIdAndCourseId
                (userId, courseId);
        if (existingInteraction != null) {
            existingInteraction.setClicks(existingInteraction.getClicks() + 1);
            existingInteraction.setUserId(userId);
            existingInteraction.setCourseId(courseId);
            existingInteraction.setPurchased(false);

            interactionRepository.save(existingInteraction);
            return InteractionResponse.fromInteraction(existingInteraction, userResponse, existingCourse);
        } else {
            Interaction interaction = Interaction.builder()
                    .courseId(courseId)
                    .userId(userId)
                    .purchased(false)
                    .clicks(1L)
                    .build();
            interactionRepository.save(interaction);
            return InteractionResponse.fromInteraction(interaction, userResponse, existingCourse);
        }
    }

    @Override
    public InteractionResponse updateInteraction(Long id) {
        Interaction existingInteraction = interactionRepository.findById(id).orElse(null);
        if (existingInteraction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interaction not found");
        }

        TestCourseResponse existingCourse = courseClient.getFullCourseResponse(existingInteraction.getCourseId());
        if (existingCourse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsername(token);
        UserResponse userResponse = userClient.getUserByIdClient(userId, token);

        existingInteraction.setUserId(userId);
        existingInteraction.setClicks(existingInteraction.getClicks());
        existingInteraction.setPurchased(true);
        existingInteraction.setCourseId(existingInteraction.getCourseId());
        interactionRepository.save(existingInteraction);
        return InteractionResponse.fromInteraction(existingInteraction, userResponse, existingCourse);

    }

    @Override
    public InteractionResponse getInteraction(Long courseId) {
        TestCourseResponse existingCourse = courseClient.getFullCourseResponse(courseId);
        if (existingCourse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsername(token);
        UserResponse userResponse = userClient.getUserByIdClient(userId, token);

        Interaction existingInteraction = interactionRepository.getInteractionByUserIdAndCourseId(userId, courseId);
        if (existingInteraction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interaction not found");
        }

        existingInteraction.setUserId(userId);
        existingInteraction.setClicks(existingInteraction.getClicks());
        existingInteraction.setPurchased(true);
        existingInteraction.setCourseId(existingInteraction.getCourseId());
        interactionRepository.save(existingInteraction);
        return InteractionResponse.fromInteraction(existingInteraction, userResponse, existingCourse);
    }
}
