package com.htt.elearning.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phone;
    private String avatar;
    private boolean isActive;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date dateOfBirth;
    @JsonProperty("facebook_account")
    private String facebookAccountId;
    @JsonProperty("google_account")
    private String googleAccountId;
    @JoinColumn(name = "role_id")
    private Long roleId;
}
