package com.htt.elearning.teacher.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponse {
    private Long id;
    private String position;
    private String description;
    private Long userId;
}
