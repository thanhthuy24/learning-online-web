package com.htt.elearning.interesting.response;

import com.htt.elearning.category.response.CategoryResponse;
import com.htt.elearning.interesting.pojo.Userinteresting;
import com.htt.elearning.user.response.UserResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInterestingResponse {
    private Long id;
    private UserResponse user;
    private CategoryResponse category;

    public static UserInterestingResponse fromUserInteresting (Userinteresting userinteresting,
                                                               UserResponse user,
                                                               CategoryResponse category) {
        UserInterestingResponse userInterestingResponse = UserInterestingResponse.builder()
                .id(userinteresting.getId())
                .user(user)
                .category(category)
                .build();
        return userInterestingResponse;
    }
}
