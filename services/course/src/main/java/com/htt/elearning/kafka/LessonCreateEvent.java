package com.htt.elearning.kafka;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonCreateEvent {
    private Long id;
    private String name;
    private Long courseId;
    private String courseName;
    private Date createdAt;
}
