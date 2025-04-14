package com.htt.elearning.teacher.response;

import com.htt.elearning.teacher.pojo.Teacher;
import com.htt.elearning.user.response.UserResponse;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponseClient {
    private Long id;
    private String position;
    private String description;
    private UserResponse user;

    public static TeacherResponseClient fromTeacher(Teacher teacher) {
        TeacherResponseClient teacherResponseClient = TeacherResponseClient.builder()
                .id(teacher.getId())
                .position(teacher.getPosition())
                .description(teacher.getDescription())
                .user(UserResponse.fromUser(teacher.getUser()))
                .build();
        return teacherResponseClient;
    }
}
