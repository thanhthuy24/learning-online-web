package com.htt.elearning.register.response;

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
    private Long userId;
}
