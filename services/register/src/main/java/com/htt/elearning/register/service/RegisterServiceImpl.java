package com.htt.elearning.register.service;

import com.htt.elearning.register.dto.RegisterDTO;
import com.htt.elearning.register.pojo.Register;
import com.htt.elearning.register.repository.RegisterRepository;
import com.htt.elearning.register.response.RegisterResponse;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService{
    private final RegisterRepository registerRepository;
    private final UserClient userClient;
    private final HttpServletRequest request;

    @Override
    public List<RegisterResponse> getAllRegisters() {
        String token = request.getHeader("Authorization");

        List<Register> registers = registerRepository.findAll();
        if (registers.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có đơn cần phê duyệt");
        List<Long> userIds = registers.stream()
                .map(Register::getUserId)
                .distinct()
                .collect(Collectors.toList());

        List<UserResponse> users = userClient.getUsersByIdsClient(userIds, token);
        Map<Long, UserResponse> usersMap = users.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));

        List<RegisterResponse> registerResponses = registers.stream()
                .map(register -> {
                    UserResponse user = usersMap.get(register.getUserId());
                    return RegisterResponse.fromRegister(register, user);
                })
                .collect(Collectors.toList());
        return registerResponses;
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
    public List<RegisterResponse> getListFormByUserId() {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsername(token);

        List<Register> registers = registerRepository.findRegisterByUserId(userId);
        UserResponse user = userClient.getUserByIdClient(userId, token);

        return registers.stream()
                .map(register -> RegisterResponse.fromRegister(register, user))
                .collect(Collectors.toList());
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
    public RegisterResponse getRegisterById(Long registerId) {
        String token = request.getHeader("Authorization");
        Register register = registerRepository.findById(registerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy ID như trên!!"));

        UserResponse user = userClient.getUserByIdClient(register.getUserId(), token);
        return RegisterResponse.fromRegister(register, user);
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
            existingRegister.setPosition(registerDTO.getPosition());
            existingRegister.setStatus(true);
            existingRegister.setReason(registerDTO.getReason());
            existingRegister.setUserId(registerDTO.getUserId());

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
