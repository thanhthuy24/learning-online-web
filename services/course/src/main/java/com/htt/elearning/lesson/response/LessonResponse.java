package com.htt.elearning.lesson.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.htt.elearning.lesson.pojo.Lesson;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonResponse {
    private Long id;
    private String name;
    private String description;
    private Date createdDate;
    private Date updatedDate;
    @JsonProperty("course_id")
    private Long courseId;
    public static LessonResponse fromLesson(Lesson lesson) {
        LessonResponse lessonResponse = LessonResponse.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .description(lesson.getDescription())
                .courseId(lesson.getCourse().getId())
                .build();
        lessonResponse.setCreatedDate(lesson.getCreatedDate());
        lessonResponse.setUpdatedDate(lesson.getUpdatedDate());
        return lessonResponse;
    }
}
