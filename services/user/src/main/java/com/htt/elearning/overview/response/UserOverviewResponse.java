package com.htt.elearning.overview.response;

import com.htt.elearning.overview.pojo.Useroverview;
import com.htt.elearning.user.response.UserResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOverviewResponse {
    private Long id;
    private String gender;
    private String job;
    private Float dailyHours;
    private Long tag;
    private UserResponse user;

    public static UserOverviewResponse fromUserOverview(Useroverview useroverview, UserResponse user) {
        UserOverviewResponse userOverviewResponse = UserOverviewResponse.builder()
                .id(useroverview.getId())
                .gender(useroverview.getGender())
                .job(useroverview.getJob())
                .dailyHours(useroverview.getDailyHours())
                .tag(useroverview.getTag())
                .user(user)
                .build();
        return userOverviewResponse;
    }
}
