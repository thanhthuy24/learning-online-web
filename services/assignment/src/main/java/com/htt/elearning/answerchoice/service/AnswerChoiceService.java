package com.htt.elearning.answerchoice.service;

import com.htt.elearning.answerchoice.dto.AnswerChoiceDTO;
import com.htt.elearning.answerchoice.pojo.Answerchoice;
import com.htt.elearning.answerchoice.response.AnswerChoiceResponse;
import com.htt.elearning.exceptions.DataNotFoundException;

import java.util.List;

public interface AnswerChoiceService {
    Answerchoice createAnswerChoice(AnswerChoiceDTO answerChoiceDTO, Long assignmentId) throws DataNotFoundException;
    List<AnswerChoiceResponse> checkAnswersByAssignmentId(Long assignmentId);
}
