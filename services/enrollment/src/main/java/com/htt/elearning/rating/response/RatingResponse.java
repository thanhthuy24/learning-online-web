package com.htt.elearning.rating.response;

import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.rating.pojo.Courserating;
import com.htt.elearning.user.response.UserResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingResponse {
    private Long id;
    private String comment;
    private Long rating;
    private Date ratingDate;
    private String sentiment;
    private UserResponse user;
    private TestCourseResponse course;

    public static RatingResponse fromRating(Courserating courserating, UserResponse user,
                                            TestCourseResponse course) {
        RatingResponse ratingResponse = RatingResponse.builder()
                .id(courserating.getId())
                .comment(courserating.getComment())
                .rating(courserating.getRating())
                .ratingDate(courserating.getRatingDate())
                .sentiment(courserating.getSentiment())
                .user(user)
                .course(course)
                .build();
        return ratingResponse;
    }
}
