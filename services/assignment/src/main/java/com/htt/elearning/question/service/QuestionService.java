package com.htt.elearning.question.service;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.question.dto.QuestionChoiceDTO;
import com.htt.elearning.question.dto.QuestionDTO;
import com.htt.elearning.question.dto.QuestionEssayDTO;
import com.htt.elearning.question.pojo.Question;

import java.util.List;

public interface QuestionService {
    List<QuestionChoiceDTO> getQuestionsByAssignmentId(Long assignmentId);
    List<QuestionEssayDTO> getQuestionEssaysByAssignmentId(Long assignmentId);
    Question createQuestion(QuestionDTO questionDTO) throws DataNotFoundException;
    Question updateQuestion(Long questionId, QuestionDTO questionDTO);
    Question getQuestionById(Long questionId);

    Long countQuestionByAssignmentId(Long assignmentId);
}
