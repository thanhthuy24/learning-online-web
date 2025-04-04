package com.htt.elearning.essay.service;
import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.assignment.repository.AssignmentRepository;
import com.htt.elearning.essay.dto.EssayDTO;
import com.htt.elearning.essay.pojo.Essay;
import com.htt.elearning.essay.repository.EssayRepository;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.question.pojo.Question;
import com.htt.elearning.question.repository.QuestionRepository;
import com.htt.elearning.user.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EssayServiceImpl implements EssayService {
    private final EssayRepository essayRepository;
    private final AssignmentRepository assignmentRepository;
    private final QuestionRepository questionRepository;
    private final UserClient userClient;

    @Override
    public Essay createEssay(EssayDTO essayDTO) throws DataNotFoundException {
        Assignment existingAssignment = assignmentRepository.findById(essayDTO.getAssignmentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find assignment by id "+essayDTO.getAssignmentId() ));

        Question existingQuestion = questionRepository.findById(essayDTO.getQuestionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find question by id "+essayDTO.getQuestionId() ));

        Essay checkEssay = essayRepository.findByQuestionId(essayDTO.getQuestionId());
        if (checkEssay != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Essay already exists");
        }

        Long userId = userClient.getUserIdByUsername();

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

        Long userId = userClient.getUserIdByUsername();

        if(existingEssay != null){
            existingEssay.setContent(essayDTO.getContent());
            existingEssay.setAssignment(existingAssignment);
            existingEssay.setQuestion(existingQuestion);
            existingEssay.setUserId(userId);
            essayRepository.save(existingEssay);
        }
        return null;
    }

//    @Override
//    public Essay getEssayByAssignmentId(Long assignmentId) {
//        Essay existingEssay = essayRepository.findByAssignment(assignmentId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "Can not find essay by id " + essayId));
//
//        return existingEssay;
//    }

    @Override
    public Page<Essay> getEssaysByAssignment(Long assignmentId, PageRequest pageRequest) {
        return essayRepository.findByAssignmentId(assignmentId, pageRequest);
    }
}
