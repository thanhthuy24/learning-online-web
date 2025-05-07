package com.htt.elearning.wishlist.response;

import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.user.response.UserResponse;
import com.htt.elearning.wishlist.pojo.Wishlist;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistResponse {
    private Long id;
    private UserResponse user;
    private TestCourseResponse course;

    public static WishlistResponse fromWishlist (Wishlist wishlist,
                                                 UserResponse user,
                                                 TestCourseResponse course) {
        WishlistResponse wishlistResponse = WishlistResponse.builder()
                .id(wishlist.getId())
                .user(user)
                .course(course)
                .build();
        return wishlistResponse;
    }

}
