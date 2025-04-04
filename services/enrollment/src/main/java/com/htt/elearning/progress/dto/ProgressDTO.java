package com.htt.elearning.progress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProgressDTO {
    private String status;
    private Float completionPercentage;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("course_id")
    private Long courseId;
}
