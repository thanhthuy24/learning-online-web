package com.htt.elearning.lesson.dtos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {
    @Size(min = 2, max = 200, message = "Lesson's name must be between 2 and 200 characters")
    @JsonProperty("name")
    private String name;
    @Size(min = 2, max = 200, message = "Description must be between 2 and 200 characters")
    @JsonProperty("description")
    private String description;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("course_id")
    private Long courseId;
    @JsonIgnore
    private List<MultipartFile> files;
}
