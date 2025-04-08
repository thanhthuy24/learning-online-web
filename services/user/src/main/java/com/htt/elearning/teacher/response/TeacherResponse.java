package com.htt.elearning.teacher.response;

import com.htt.elearning.teacher.pojo.Teacher;
import com.htt.elearning.user.response.UserResponse;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponse {
    private Long id;
    private String position;
    private String description;
    private Long userId;

    public static TeacherResponse fromTeacher(Teacher teacher) {
        TeacherResponse teacherResponse = TeacherResponse.builder()
                .id(teacher.getId())
                .position(teacher.getPosition())
                .description(teacher.getDescription())
                .userId(teacher.getUserId())
                .build();
        return teacherResponse;
    }
}
