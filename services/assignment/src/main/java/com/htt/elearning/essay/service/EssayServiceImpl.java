package com.htt.elearning.essay.service;
import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.assignment.repository.AssignmentRepository;
import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.enrollment.EnrollmentClient;
import com.htt.elearning.essay.dto.EssayDTO;
import com.htt.elearning.essay.pojo.Essay;
import com.htt.elearning.essay.repository.EssayRepository;
import com.htt.elearning.essay.response.EssayResponseClient;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.question.pojo.Question;
import com.htt.elearning.question.repository.QuestionRepository;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EssayServiceImpl implements EssayService {
    private final EssayRepository essayRepository;
    private final AssignmentRepository assignmentRepository;
    private final QuestionRepository questionRepository;
    private final UserClient userClient;
    private final HttpServletRequest request;
    private final EnrollmentClient enrollmentClient;

    @Override
    public Essay createEssay(EssayDTO essayDTO) throws DataNotFoundException {
        Assignment existingAssignment = assignmentRepository.findById(essayDTO.getAssignmentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find assignment by id "+ essayDTO.getAssignmentId() ));

        Question existingQuestion = questionRepository.findById(essayDTO.getQuestionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find question by id "+ essayDTO.getQuestionId() ));

        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        Essay checkEssay = essayRepository.findByQuestionIdAndUserId(essayDTO.getQuestionId(), userId);
        if (checkEssay != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Essay already exists");
        }

        Boolean checkEnroll = enrollmentClient.checkEnrollment(userId, existingAssignment.getCourseId(), token);
        if (!checkEnroll) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must enroll the course first!!");
        }

        Essay newEssay = Essay.builder()
                .content(essayDTO.getContent())
                .assignment(existingAssignment)
                .question(existingQuestion)
                .userId(userId)
                .build();
        essayRepository.save(newEssay);
        return newEssay;
    }

    @Override
    public Essay updateEssay(Long essayId, EssayDTO essayDTO) throws DataNotFoundException {
        Essay existingEssay = essayRepository.findById(essayId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find essay by id " + essayId));

        Assignment existingAssignment = assignmentRepository.findById(essayDTO.getAssignmentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find assignment by id "+essayDTO.getAssignmentId() ));

        Question existingQuestion = questionRepository.findById(essayDTO.getQuestionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find question by id "+essayDTO.getQuestionId() ));

        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        if(existingEssay != null){
            existingEssay.setContent(essayDTO.getContent());
            existingEssay.setAssignment(existingAssignment);
            existingEssay.setQuestion(existingQuestion);
            existingEssay.setUserId(userId);
            essayRepository.save(existingEssay);
        }
        return null;
    }

    @Override
    public Page<EssayResponseClient> getEssaysByAssignment(Long assignmentId, PageRequest pageRequest) {
        String token = request.getHeader("Authorization");
        Page<Essay> essayPage = essayRepository.findByAssignmentId(assignmentId, pageRequest);
        List<Essay> essays = essayPage.getContent();

        List<Long> userIds = essays.stream()
                .map(Essay::getUserId)
                .distinct()
                .collect(Collectors.toList());

        List<UserResponse> users = userClient.getUsersByIdsClient(userIds, token);
        Map<Long, UserResponse> userMap = users.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));

        List<EssayResponseClient> essaysResponse = essays.stream()
                .map(essay -> {
                    UserResponse user = userMap.get(essay.getUserId());
                    return EssayResponseClient.fromEssayClient(essay, user);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(essaysResponse, pageRequest, essayPage.getTotalElements());

    }
}
