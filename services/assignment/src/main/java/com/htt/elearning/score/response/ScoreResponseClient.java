package com.htt.elearning.score.response;

import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.assignment.response.AssignmentFullResponse;
import com.htt.elearning.score.pojo.Score;
import com.htt.elearning.user.response.UserResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreResponseClient {
    private Long id;
    private String feedBack;
    private Float score;
    private Assignment assignment;
    private UserResponse user;

    public static ScoreResponseClient fromScore(Score score, UserResponse userResponse) {
        ScoreResponseClient scoreResponseClient = ScoreResponseClient.builder()
                .id(score.getId())
                .score(score.getScore())
                .feedBack(score.getFeedBack())
                .assignment(score.getAssignment())
                .user(userResponse)
                .build();
        return scoreResponseClient;
    }
}
