package com.htt.elearning.answerchoice.service;
import com.htt.elearning.answerchoice.dto.AnswerChoiceDTO;
import com.htt.elearning.answerchoice.pojo.Answerchoice;
import com.htt.elearning.answerchoice.repository.AnswerChoiceRepository;
import com.htt.elearning.answerchoice.response.AnswerChoiceResponse;
import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.assignment.repository.AssignmentRepository;
import com.htt.elearning.choice.pojo.Choice;
import com.htt.elearning.choice.repository.ChoiceRepository;
import com.htt.elearning.enrollment.EnrollmentClient;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.question.pojo.Question;
import com.htt.elearning.question.repository.QuestionRepository;
import com.htt.elearning.user.UserClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerChoiceServiceImpl implements AnswerChoiceService {
    private final AnswerChoiceRepository answerChoiceRepository;
    private final QuestionRepository questionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserClient userClient;
    private final ChoiceRepository choiceRepository;
    private final EnrollmentClient enrollmentClient;
    private final HttpServletRequest request;

    @Override
    public Answerchoice createAnswerChoice(AnswerChoiceDTO answerChoiceDTO, Long assignmentId) throws DataNotFoundException {
        Assignment existingAssignment = assignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find Assignment with id " + assignmentId));

        LocalDateTime dueDate = Instant.ofEpochMilli(existingAssignment.getDueDate().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // Kiểm tra nếu dueDate đã qua hạn
        if (dueDate.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The due date for this assignment has passed. You cannot submit an answer choice.");
        }

        Question existingQuestion = questionRepository
                .findById(answerChoiceDTO.getQuestionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find question with id " + answerChoiceDTO.getQuestionId()));

        Choice existingChoice = choiceRepository
                .findById(answerChoiceDTO.getChoiceId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find question with id " + answerChoiceDTO.getChoiceId()));

        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        Boolean checkEnrollment = enrollmentClient.checkEnrollment(userId, existingAssignment.getCourseId(), token);
        if (checkEnrollment == null || !checkEnrollment) {
            new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This course isn't enrolled in your list! Please enroll before participating in this course!!");
        }

        Optional<Answerchoice> checkAnswerchoice = answerChoiceRepository
                .findByAssignmentIdAndQuestionIdAndUserId
                        (
                                assignmentId,
                                existingQuestion.getId(),
                                userId);
        if (checkAnswerchoice.isPresent()) {
            throw new DataNotFoundException("This answer choice already exists!");
        }

        Answerchoice newAnswer = Answerchoice.builder()
                .choice(existingChoice)
                .assignment(existingAssignment)
                .question(existingQuestion)
                .userId(userId)
                .build();
        answerChoiceRepository.save(newAnswer);
        return newAnswer;
    }

    @Override
    public List<AnswerChoiceResponse> checkAnswersByAssignmentId(Long assignmentId) {
        Assignment existingAssignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find Assignment with id " + assignmentId));

        List<Question> existingQuestions = questionRepository.findByAssignmentId(assignmentId);
        List<Choice> existingChoice = choiceRepository.findByQuestionIn(existingQuestions);

        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        Boolean checkEnrollment = enrollmentClient.checkEnrollment(userId, existingAssignment.getCourseId(), token);
        if (checkEnrollment == null || !checkEnrollment) {
            new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This course isn't enrolled in your list! Please enroll before participating in this course!!");
        }

        List<Answerchoice> list = answerChoiceRepository.findByAssignmentIdAndUserId(assignmentId, userId);
        return list.stream()
                .map(this::convertToResponse)  // Sử dụng phương thức chuyển đổi
                .collect(Collectors.toList());
    }

    private AnswerChoiceResponse convertToResponse(Answerchoice answerChoice) {
        return AnswerChoiceResponse.builder()
                .id(answerChoice.getId())
                .assignmentId(answerChoice.getAssignment().getId())
                .questionId(answerChoice.getQuestion().getId())
                .userId(answerChoice.getUserId())
                .choiceId(answerChoice.getChoice().getId())
                .build();
    }
}
