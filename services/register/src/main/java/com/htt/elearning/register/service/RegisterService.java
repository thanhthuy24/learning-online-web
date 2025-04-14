package com.htt.elearning.register.service;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.register.dto.RegisterDTO;
import com.htt.elearning.register.pojo.Register;
import com.htt.elearning.register.response.RegisterResponse;

import java.util.List;

public interface RegisterService {
    List<RegisterResponse> getAllRegisters();
    List<Register> getRegisterInActive();
    List<Register> getRegisterActive();

    List<RegisterResponse> getListFormByUserId();

    Register getRegisterByUserId(Long userId);
    Register getRegisterByUser(Long userId);
    RegisterResponse getRegisterById(Long registerId);
    Register createRegister(RegisterDTO registerDTO);
    Register updateRegister(RegisterDTO registerDTO, Long id);
}
