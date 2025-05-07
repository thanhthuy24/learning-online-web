package com.htt.elearning.course.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendDTO {
    private String name;
    private Long tag_id;
    private List<Long> category_id;
    private Float duration_hours;
}
