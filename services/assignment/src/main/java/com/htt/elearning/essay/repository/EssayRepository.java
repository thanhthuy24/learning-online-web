package com.htt.elearning.essay.repository;

import com.htt.elearning.essay.pojo.Essay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EssayRepository extends JpaRepository<Essay, Long> {
    Page<Essay> findByAssignmentId(Long assignmentId, PageRequest pageRequest);
    Essay findByQuestionId(Long questionId);
}
