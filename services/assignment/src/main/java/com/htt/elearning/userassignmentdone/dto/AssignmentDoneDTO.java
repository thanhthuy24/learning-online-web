package com.htt.elearning.userassignmentdone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDoneDTO {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("assignment_id")
    private Long assignmentId;
}
