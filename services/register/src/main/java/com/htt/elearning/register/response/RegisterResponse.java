package com.htt.elearning.register.response;

import com.htt.elearning.register.pojo.Register;
import com.htt.elearning.user.response.UserResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private Long id;
    private String reason;
    private String position;
    private Boolean status;
    private UserResponse user;

    public static RegisterResponse fromRegister(Register register, UserResponse user) {
        RegisterResponse registerResponse = RegisterResponse.builder()
                .id(register.getId())
                .reason(register.getReason())
                .position(register.getPosition())
                .status(register.getStatus())
                .user(user)
                .build();
        return registerResponse;
    }
}
