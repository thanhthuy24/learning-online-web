package com.htt.elearning.assignment.service;

import com.htt.elearning.assignment.dto.AssignmentDTO;
import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.assignment.repository.AssignmentRepository;
import com.htt.elearning.assignment.response.AssignmentResponse;
import com.htt.elearning.course.CourseClient;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.enrollment.EnrollmentClient;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.kafka.AssignmentCreateEvent;
import com.htt.elearning.kafka.AssignmentProducer;
import com.htt.elearning.lesson.LessonClient;
import com.htt.elearning.lesson.response.LessonResponse;
import com.htt.elearning.tag.TagClient;
import com.htt.elearning.tag.response.TagResponse;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final CourseClient courseClient;
    private final LessonClient lessonClient;
    private final AssignmentRepository assignmentRepository;
    private final TagClient tagClient;
    private final UserClient userClient;
    private final EnrollmentClient enrollmentClient;
    private final AssignmentProducer assignmentProducer;
    private final HttpServletRequest request;

    @Override
    public Page<AssignmentResponse> getAllAssignment(PageRequest pageRequest) {
        return assignmentRepository
                .findAll(pageRequest)
                .map(AssignmentResponse::fromAssignment);
    }

    @Override
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find Assignment with id " + id));
    }

    @Override
    public Assignment createAssignment(AssignmentDTO assignmentDTO) throws DataNotFoundException {
        TagResponse existTag = tagClient.getTagById(assignmentDTO.getTagId());
        if (existTag == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }

        CourseResponse existCourse = courseClient.getCourseByIdClient(assignmentDTO.getCourseId());
        if (existCourse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found by id: " + assignmentDTO.getCourseId());
        }

        LessonResponse existLesson = lessonClient.getLessonById(assignmentDTO.getLessonId());
        if (existLesson == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found by id: " + assignmentDTO.getLessonId());
        }

        Assignment newAssignment = Assignment.builder()
                .name(assignmentDTO.getName())
                .tagId(assignmentDTO.getTagId())
                .courseId(assignmentDTO.getCourseId())
                .lessonId(assignmentDTO.getLessonId())
                .dueDate(assignmentDTO.getDueDate())
                .build();

        String token = request.getHeader("Authorization");

        List<UserResponse> users = enrollmentClient.getUsersByCourseIdClient(assignmentDTO.getCourseId(), token);

        Assignment saveAssignment = assignmentRepository.save(newAssignment);

        assignmentProducer.sendAssignmentCreateEvent(
                AssignmentCreateEvent.builder()
                        .id(saveAssignment.getId())
                        .name(saveAssignment.getName())
                        .courseId(saveAssignment.getCourseId())
                        .courseName(existCourse.getName())
                        .createdAt(new Date())
                .build(), token);

        return newAssignment;
    }

    @Override
    public List<Assignment> getAssignmentByLessonId(Long lessonId) {
        return assignmentRepository.findByLessonId(lessonId);
    }

    @Override
    public List<Assignment> getAssignmentByCourseId(Long courseId) {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        Boolean checkEnrollment = enrollmentClient.checkEnrollment(userId, courseId, token);
        if (checkEnrollment == null || !checkEnrollment) {
            new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This course isn't enrolled in your list! Please enroll before participating in this course!!");
        }
        return assignmentRepository.findByCourseId(courseId);
    }

    @Override
    public Assignment updateAssignment(Long id, AssignmentDTO assignmentDTO) throws DataNotFoundException {
        Assignment existingAssignment = getAssignmentById(id);
        if(existingAssignment != null) {

            TagResponse existTag = tagClient.getTagById(assignmentDTO.getTagId());
            if (existTag == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
            }

            CourseResponse existCourse = courseClient.getCourseByIdClient(assignmentDTO.getCourseId());
            if (existCourse == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found by id: " + assignmentDTO.getCourseId());
            }

            LessonResponse existLesson = lessonClient.getLessonById(assignmentDTO.getLessonId());
            if (existLesson == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found by id: " + assignmentDTO.getLessonId());
            }
            existingAssignment.setName(assignmentDTO.getName());
            existingAssignment.setCourseId(assignmentDTO.getCourseId());
            existingAssignment.setLessonId(assignmentDTO.getLessonId());
            existingAssignment.setTagId(assignmentDTO.getTagId());
            existingAssignment.setDueDate(assignmentDTO.getDueDate());
            return assignmentRepository.save(existingAssignment);

        }

        return null;
    }

    @Override
    public void deleteAssignment(Long id) {
    }
    }
