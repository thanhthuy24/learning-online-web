package com.htt.elearning.register.repository;

import com.htt.elearning.register.pojo.Register;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegisterRepository extends JpaRepository<Register, Long> {
    List<Register> findByStatus(Boolean status);
    Register findByUserId(Long userId);
    List<Register> findRegisterByUserId(Long userId);

}
