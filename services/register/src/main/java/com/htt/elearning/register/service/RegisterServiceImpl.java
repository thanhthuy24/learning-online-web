package com.htt.elearning.register.service;

import com.htt.elearning.register.dto.RegisterDTO;
import com.htt.elearning.register.pojo.Register;
import com.htt.elearning.register.repository.RegisterRepository;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService{
    private final RegisterRepository registerRepository;
    private final UserClient userClient;
    private final HttpServletRequest request;

    @Override
    public List<Register> getAllRegisters() {
        List<Register> list = registerRepository.findAll();
        if (list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có đơn cần phê duyệt");
        return list;
    }

    @Override
    public List<Register> getRegisterInActive() {
        List<Register> list = registerRepository.findByStatus(false);
        if (list.isEmpty())
            return null;
        return list;
    }

    @Override
    public List<Register> getRegisterActive() {
        List<Register> list = registerRepository.findByStatus(true);
        if (list.isEmpty())
            return null;
        return list;
    }

    @Override
    public List<Register> getListFormByUserId() {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsername(token);

        List<Register> list = registerRepository.findRegisterByUserId(userId);

        return list;
    }

    @Override
    public Register getRegisterByUserId(Long userId) {
        if (registerRepository.findByUserId(userId) == null) {
            return null;
        }
        Register r = registerRepository.findByUserId(userId);
        if(r.getStatus() == true){
            return r;
        }
        return null;
    }

    public Register getRegisterByUser(Long userId) {
        Register r = registerRepository.findByUserId(userId);
        if (r != null) {
            return r;
        }
        return null;
    }

    @Override
    public Register getRegisterById(Long registerId) {
        return registerRepository.findById(registerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy ID như trên!!"));
    }

    @Override
    public Register createRegister(RegisterDTO registerDTO) {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsername(token);

        Register newRegister = Register.builder()
                .reason(registerDTO.getReason())
                .position(registerDTO.getPosition())
                .status(false)
                .userId(userId)
                .build();
        registerRepository.save(newRegister);
        return newRegister;
    }

    @Override
    public Register updateRegister(RegisterDTO registerDTO, Long id) {
        String token = request.getHeader("Authorization");
        Register existingRegister = registerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        if (existingRegister != null) {
            UserResponse user = userClient.getUserByIdClient(registerDTO.getUserId(), token);

            if (user != null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            existingRegister.setPosition(registerDTO.getPosition());
            existingRegister.setStatus(true);
            existingRegister.setReason(registerDTO.getReason());
            existingRegister.setUserId(user.getId());

//            userClient.updateRoleTeacher(user.getId());
//
//            Teacher newTeacher = Teacher.builder()
//                    .description(registerDTO.getReason())
//                    .position(registerDTO.getPosition())
//                    .user(existingUser)
//                    .build();
//            teacherRepository.save(newTeacher);
            return registerRepository.save(existingRegister);
        }
        return null;
    }
}
