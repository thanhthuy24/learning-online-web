package com.htt.elearning.lesson.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonResponse {
    private String name;
    private String description;
    private Date createdDate;
    private Date updatedDate;
    @JsonProperty("course_id")
    private Long courseId;
}
