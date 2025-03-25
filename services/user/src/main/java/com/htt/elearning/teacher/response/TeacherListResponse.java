package com.htt.elearning.teacher.response;

import com.htt.elearning.teacher.pojo.Teacher;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherListResponse {
    private List<Teacher> teachers;
    private int totalPages;
}
