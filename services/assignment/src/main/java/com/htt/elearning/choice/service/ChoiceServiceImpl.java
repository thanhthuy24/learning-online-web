package com.htt.elearning.choice.service;

import com.htt.elearning.choice.dto.ChoiceDTO;
import com.htt.elearning.choice.pojo.Choice;
import com.htt.elearning.choice.repository.ChoiceRepository;
import com.htt.elearning.question.pojo.Question;
import com.htt.elearning.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChoiceServiceImpl implements ChoiceService {
    private final ChoiceRepository choiceRepository;
    private final QuestionRepository questionRepository;

    @Override
    public Choice createChoice(ChoiceDTO choiceDTO) {
//        Question existingQuestion = questionRepository.findById(choiceDTO.getQuestionId());
        Question existingQuestion = questionRepository.findById(choiceDTO.getQuestionId()).get();
        if (existingQuestion != null) {
            Choice choice = Choice.builder()
                    .content(choiceDTO.getContent())
                    .question(existingQuestion)
                    .isCorrect(choiceDTO.isCorrect())
                    .build();
            return choiceRepository.save(choice);
        }
        return null;
    }

    @Override
    public Choice updateChoice(Long choiceId, ChoiceDTO choiceDTO) {
        Choice existingChoice = choiceRepository.findById(choiceId).get();
        if (existingChoice != null) {
            Question existingQuestion = questionRepository.findById(choiceDTO.getQuestionId()).get();
            existingChoice.setContent(choiceDTO.getContent());
            existingChoice.setQuestion(existingQuestion);
            existingChoice.setIsCorrect(choiceDTO.isCorrect());
            return choiceRepository.save(existingChoice);
        }
        return null;
    }
}
