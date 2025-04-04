package com.htt.elearning.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "Username must be required")
    private String username;
    @NotBlank(message = "Password must be required")
    private String password;
}
