package com.htt.elearning.teacher.service;

import com.htt.elearning.teacher.dto.TeacherDTO;
import com.htt.elearning.teacher.pojo.Teacher;
import com.htt.elearning.teacher.response.TeacherResponse;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import org.springframework.data.domain.*;

import java.util.List;

public interface TeacherService {
    Teacher createTeacher(TeacherDTO teacherDTO);
    Teacher getTeacherById(Long id);
    TeacherDTO getTeacherDTOById(Long id);
    Page<Teacher> getAllTeachers(Pageable pageable);
    Teacher updateTeacher(Long id, TeacherDTO teacherDTO);
    void deleteTeacher(Long id);
    Teacher getTeacherByUserId(Long userId);
    List<Teacher> getAll();

    Teacher getInformation(Long teacherId);

//    techer-client
    TeacherResponse getTeacherByIdClient(Long id);
    TeacherResponse getTeacherByUserIdClient(Long userId);

    TeacherResponseClient getOneTeacherResponseClient(Long teacherId);
    List<TeacherResponseClient> getTeacherResponseClient(List<Long> teacherIds);
}
