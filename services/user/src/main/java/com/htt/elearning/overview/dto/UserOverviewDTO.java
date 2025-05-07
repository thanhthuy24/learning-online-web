package com.htt.elearning.overview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOverviewDTO {
    private String gender;
    private String job;
    @JsonProperty("daily_hours")
    private Float dailyHours;
    @JsonProperty("tag")
    private Long tagId;
    @JsonProperty("user_id")
    private Long userId;
}
