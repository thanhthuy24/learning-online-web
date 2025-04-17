package com.htt.elearning.teacher;

import com.htt.elearning.teacher.response.TeacherResponseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class TeacherClient2Fallback implements TeacherClient2 {

    @Override
    public List<TeacherResponseClient> getInformationTeacher(List<Long> teacherIds) {
        log.warn("Fallback: Cannot get teacher list for ids {}", teacherIds);
        return Collections.emptyList(); // or throw custom exception
    }

    @Override
    public TeacherResponseClient getInformationOneTeacher(Long teacherId) {
        log.warn("Fallback: Cannot get teacher info for id {}", teacherId);
        return null; // or a default object
    }
}

