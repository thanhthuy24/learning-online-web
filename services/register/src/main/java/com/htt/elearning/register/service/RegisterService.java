package com.htt.elearning.register.service;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.register.dto.RegisterDTO;
import com.htt.elearning.register.pojo.Register;

import java.util.List;

public interface RegisterService {
    List<Register> getAllRegisters();
    List<Register> getRegisterInActive();
    List<Register> getRegisterActive();

    List<Register> getListFormByUserId();

    Register getRegisterByUserId(Long userId);
    Register getRegisterByUser(Long userId);
    Register getRegisterById(Long registerId);
    Register createRegister(RegisterDTO registerDTO);
    Register updateRegister(RegisterDTO registerDTO, Long id);
}
