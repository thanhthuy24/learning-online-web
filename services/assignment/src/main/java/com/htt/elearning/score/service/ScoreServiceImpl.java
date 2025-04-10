package com.htt.elearning.score.service;
import com.htt.elearning.answerchoice.pojo.Answerchoice;
import com.htt.elearning.answerchoice.repository.AnswerChoiceRepository;
import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.assignment.repository.AssignmentRepository;
import com.htt.elearning.enrollment.EnrollmentClient;
import com.htt.elearning.essay.pojo.Essay;
import com.htt.elearning.essay.repository.EssayRepository;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.notification.NotificationClient;
import com.htt.elearning.notification.dto.NotificationDTO;
import com.htt.elearning.question.repository.QuestionRepository;
import com.htt.elearning.score.dto.ScoreDTO;
import com.htt.elearning.score.pojo.Score;
import com.htt.elearning.score.repository.ScoreRepository;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final UserClient userClient;
    private final AssignmentRepository assignmentRepository;
    private final AnswerChoiceRepository answerChoiceRepository;
    private final QuestionRepository questionRepository;
    private final EssayRepository essayRepository;
    private final NotificationClient notificationClient;
    private final EnrollmentClient enrollmentClient;
    private final HttpServletRequest request;

    @Override
    public List<Score> getScoreByAssignmentId(Long assignmentId) throws DataNotFoundException {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.getUserByUsername(username);

        Assignment existingAssignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Assignment not found!!"));

        List<Score> scores = scoreRepository.findByAssignmentId(assignmentId);
        if (scores.isEmpty()) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Score not found!!!");
        }
        return scores;
    }

    @Override
    public Score createScoreEssay(ScoreDTO scoreDTO, Long essayId) throws DataNotFoundException {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.getUserByUsername(username);
//        if (user.getRole().getName().equals("TEACHER")){
        String token = request.getHeader("Authorization");

        Long role = userClient.getRoleIdClient(token);
        if (role == 3){
            Essay existingEssay = essayRepository.findById(essayId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Essay not found!!"));
//
            Assignment existingAssignment = assignmentRepository
                    .findById(scoreDTO.getAssignmentId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Assignment not found!!"));

            Optional<Score> existingScore = scoreRepository
                    .findByUserIdAndAssignmentId(existingEssay.getUserId(), existingAssignment.getId());
            if (existingScore.isPresent()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Score already exist!!!");
            }

            Score newScore = Score.builder()
                    .score(scoreDTO.getScore())
                    .feedBack(scoreDTO.getFeedback())
                    .assignment(existingAssignment)
                    .userId(existingEssay.getUserId())
                    .build();
//            String token = request.getHeader("Authorization");
            List<UserResponse> users = enrollmentClient.getUsersByCourseIdClient(existingAssignment.getCourseId(), token);

            users.forEach(user -> {
                NotificationDTO notificationDTO = NotificationDTO.builder()
                        .title("Giảng viên vừa chấm điểm bài tập của bạn!")
                        .message("Bài tập essay: " + existingAssignment.getName()
                                + " vừa được chấm, hãy check ngay nào, " + user.getUsername() + " ơi!")
                        .userId(user.getId())
                        .build();

                notificationClient.createNotification(notificationDTO);
            });

            scoreRepository.save(newScore);
            return newScore;
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to add score");
        }
    }

    @Override
    public Optional<Score> getScoreByAssignmentIdAndUserId(Long assignmentId, Long userId) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.getUserByUsername(username);
//        if (user.getRole().getName().equals("TEACHER")){
        String token = request.getHeader("Authorization");
        Long role = userClient.getRoleIdClient(token);
        if (role == 3){
            Assignment existingAssignment = assignmentRepository
                    .findById(assignmentId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Assignment not found!!"));

            Optional<Score> existingScore = scoreRepository
                    .findByUserIdAndAssignmentId(userId, existingAssignment.getId());
            if (existingScore != null) {
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Score already exist!!!");
            }
            return existingScore;
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to add score");
        }
    }

    @Override
    public Score createScore(ScoreDTO scoreDTO) throws DataNotFoundException {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.getUserByUsername(username);
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);
        Assignment existingAssignment = assignmentRepository
                .findById(scoreDTO.getAssignmentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Assignment not found!!"));

        Optional<Score> existingScore = scoreRepository
                .findByUserIdAndAssignmentId(userId, existingAssignment.getId());
        if (existingScore.isPresent()) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Score already exist!!!");
        }

        List<Answerchoice> answerchoiceList = answerChoiceRepository
                .findByAssignmentIdAndUserId(existingAssignment.getId(), userId);

        if (answerchoiceList.isEmpty()) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer choice not found!!!");
        }

        Long countQues = questionRepository.countQuestionsByAssignmentId(existingAssignment.getId());

        float score = 0L;
        for (Answerchoice answer : answerchoiceList){
            if (answer.getChoice().getIsCorrect())
                score++;
        }

        double percentage = (double) score / countQues;
        String feedback = "";
        if (percentage >= 0.8){
            feedback = "Bravo!!!";
        } else if (percentage >= 0.6){
            feedback = "Good!";
        } else if (percentage < 0.6){
            feedback = "You need to be more careful!";
        }

        Score newScore = Score.builder()
                .score(score)
                .feedBack(feedback)
                .assignment(existingAssignment)
                .userId(userId)
                .build();

        scoreRepository.save(newScore);
        return newScore;
    }
}
