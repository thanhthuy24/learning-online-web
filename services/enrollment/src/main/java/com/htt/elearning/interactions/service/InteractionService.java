package com.htt.elearning.interactions.service;

import com.htt.elearning.interactions.dto.InteractionDTO;
import com.htt.elearning.interactions.pojo.Interaction;
import com.htt.elearning.interactions.response.InteractionResponse;

import java.util.List;

public interface InteractionService {
    InteractionResponse createInteraction(Long courseId);
    InteractionResponse updateInteraction(Long id);
    InteractionResponse getInteraction(Long courseId);
}
