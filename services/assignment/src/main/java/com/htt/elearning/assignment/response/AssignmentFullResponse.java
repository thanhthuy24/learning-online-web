package com.htt.elearning.assignment.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.lesson.response.LessonResponse;
import com.htt.elearning.tag.response.TagResponse;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentFullResponse {
    private Long id;
    private String name;
    private Date createdDate;
    private Date dueDate;
    private TagResponse tag;
    private TestCourseResponse course;
    private LessonResponse lesson;

    public static AssignmentFullResponse fromAssignment(Assignment assignment, TagResponse tagResponse,
                                                        TestCourseResponse courseResponse, LessonResponse lessonResponse) {
        AssignmentFullResponse assignmentFullResponse = AssignmentFullResponse.builder()
                .id(assignment.getId())
                .name(assignment.getName())
                .createdDate(assignment.getCreatedDate())
                .dueDate(assignment.getDueDate())
                .tag(tagResponse)
                .course(courseResponse)
                .lesson(lessonResponse)
                .build();
        return assignmentFullResponse;
    }
}
