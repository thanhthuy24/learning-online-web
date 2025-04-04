package com.htt.elearning.choice.service;

import com.htt.elearning.choice.dto.ChoiceDTO;
import com.htt.elearning.choice.pojo.Choice;

public interface ChoiceService {
    Choice createChoice(ChoiceDTO choiceDTO);
    Choice updateChoice(Long choiceId, ChoiceDTO choiceDTO);
}
