package com.htt.elearning.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String firstName;
    private String lastName;
    @NotBlank(message = "Email must be required")
    private String email;
    @NotBlank(message = "Username must be required")
    private String username;
    @NotBlank(message = "Password must be required")
    private String password;
    private String retypePassword;
    private String phone;
    private String avatar;
    private Date dateOfBirth;
    @JsonProperty("facebook_account")
    private String facebookAccountId;
    @JsonProperty("google_account")
    private String googleAccountId;
    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private Long roleId;

    @JsonIgnore
    private MultipartFile file;

}
