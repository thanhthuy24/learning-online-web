package com.htt.elearning.assignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDTO {
    @NotNull
    @Size(min = 2, max = 255, message = "Assignment's name must be between 2 and 255 characters")
    private String name;
    @Column(name = "dueDate")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date dueDate;
    @JsonProperty("tag_id")
    private Long tagId;
    @JsonProperty("course_id")
    private Long courseId;
    @JsonProperty("lesson_id")
    private Long lessonId;
}
