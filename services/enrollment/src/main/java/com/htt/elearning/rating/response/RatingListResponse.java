package com.htt.elearning.rating.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.htt.elearning.rating.pojo.Courserating;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingListResponse {
    private List<RatingResponse> ratings;
    private int totalPages;
}
