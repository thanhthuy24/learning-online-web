package com.htt.elearning.teacher.service;

import com.htt.elearning.teacher.dto.TeacherDTO;
import com.htt.elearning.teacher.pojo.Teacher;
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
}
