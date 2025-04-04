package com.htt.elearning.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phone;
    private String avatar;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date dateOfBirth;
    @JsonProperty("facebook_account")
    private String facebookAccountId;
    @JsonProperty("google_account")
    private String googleAccountId;

    @JsonIgnore
    private MultipartFile file;
}