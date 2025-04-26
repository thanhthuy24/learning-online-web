package com.htt.elearning.essay.response;

import com.htt.elearning.essay.pojo.Essay;
import com.htt.elearning.user.response.UserResponse;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EssayResponseClient {
    private Long id;
    private String content;
    private Date createdDate;
    private Long assignmentId;
    private Long questionId;
    private UserResponse user;

    public static EssayResponseClient fromEssayClient(Essay essay, UserResponse userResponse) {
        EssayResponseClient essayResponseClient = EssayResponseClient.builder()
                .id(essay.getId())
                .content(essay.getContent())
                .createdDate(essay.getCreatedDate())
                .assignmentId(essay.getAssignment().getId())
                .questionId(essay.getQuestion().getId())
                .user(userResponse)
                .build();
        return essayResponseClient;
    }

}
