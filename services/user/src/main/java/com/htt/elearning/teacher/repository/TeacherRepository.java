package com.htt.elearning.teacher.repository;

import com.htt.elearning.teacher.pojo.Teacher;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Page<Teacher> findAll(Pageable pageable); //phân trang

    @Query("SELECT t FROM Teacher t WHERE t.user.id = :userId")
    Teacher findByUserId(Long userId);
}
