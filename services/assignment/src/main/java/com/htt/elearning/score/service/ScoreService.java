package com.htt.elearning.score.service;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.score.dto.ScoreDTO;
import com.htt.elearning.score.pojo.Score;
import com.htt.elearning.score.response.ScoreResponseClient;

import java.util.List;
import java.util.Optional;

public interface ScoreService {
    Score createScore(ScoreDTO scoreDTO) throws DataNotFoundException;
    List<ScoreResponseClient> getScoreByAssignmentId(Long assignmentId) throws DataNotFoundException;
    Score createScoreEssay(ScoreDTO scoreDTO, Long essayId) throws DataNotFoundException;
    Optional<Score> getScoreByAssignmentIdAndUserId(Long assignmentId, Long userId);
}
