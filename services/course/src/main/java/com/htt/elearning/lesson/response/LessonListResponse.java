package com.htt.elearning.lesson.response;

import com.htt.elearning.lesson.pojo.Lesson;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonListResponse {
    private List<Lesson> lessons;
    private int totalPages;
}