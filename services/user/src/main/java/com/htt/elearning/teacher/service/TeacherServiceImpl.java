package com.htt.elearning.teacher.service;

import com.htt.elearning.teacher.dto.TeacherDTO;
import com.htt.elearning.teacher.pojo.Teacher;
import com.htt.elearning.teacher.repository.TeacherRepository;
import com.htt.elearning.user.pojo.User;
import com.htt.elearning.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    @Override
    public Teacher createTeacher(TeacherDTO teacherDTO) {
        User existUser = userRepository
                .findById(teacherDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not find user by id: " + teacherDTO.getUserId()));

        Teacher newTeacher = Teacher.builder()
                .position(teacherDTO.getPosition())
                .description(teacherDTO.getDescription())
                .user(existUser)
                .build();

        return teacherRepository.save(newTeacher);
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not find teacher by id: " + id));
    }

    @Override
    public TeacherDTO getTeacherDTOById(Long id) {
        return null;
    }

    @Override
    public Page<Teacher> getAllTeachers(Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }

    @Override
    public Teacher updateTeacher(Long id, TeacherDTO teacherDTO) {
        Teacher existingTeacher = getTeacherById(id);
        if(existingTeacher != null) {
            User existingUser = userRepository.findById(teacherDTO.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not find user by id: " + teacherDTO.getUserId()));
            existingTeacher.setPosition(teacherDTO.getPosition());
            existingTeacher.setDescription(teacherDTO.getDescription());
            existingTeacher.setUser(existingUser);

            return teacherRepository.save(existingTeacher);
        }
        return null;
    }

    @Override
    public void deleteTeacher(Long id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        teacherRepository.delete(optionalTeacher.get());
    }

    @Override
    public Teacher getTeacherByUserId(Long userId) {
        Teacher teacher = teacherRepository.findByUserId(userId);
        if (teacher == null) {
            return null;
        }
        return teacher;
    }

    @Override
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }
}
