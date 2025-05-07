package com.htt.elearning.interesting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInterestingDTO {
    @JsonProperty("user_id")
    private Long userId;

    private List<Long> categoryIds;
}
