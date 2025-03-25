package com.htt.elearning.course.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    @NotNull
    @Size(min = 2, max = 200, message = "Course's name must be between 2 and 200 characters")
    private String name;
    private String description;
    private String image;
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    private float price;
    private float discount;
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("tag_id")
    private Long tagId;
    @JsonProperty("teacher_id")
    private Long teacherId;

    //    private List<MultipartFile> files;
    @JsonIgnore
    private MultipartFile file;
}
