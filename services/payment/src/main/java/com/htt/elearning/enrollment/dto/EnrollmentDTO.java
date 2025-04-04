package com.htt.elearning.enrollment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDTO {
    private Date enrollmentDate;
    @JsonProperty("course_id")
    private Long courseId;
}
