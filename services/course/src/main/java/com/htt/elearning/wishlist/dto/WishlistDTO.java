package com.htt.elearning.wishlist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistDTO {
//    @JsonProperty("user_id")
//    private Long userId;
    @JsonProperty("course_id")
    private Long courseId;
}
