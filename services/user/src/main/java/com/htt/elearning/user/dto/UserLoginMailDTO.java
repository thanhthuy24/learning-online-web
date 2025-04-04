package com.htt.elearning.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginMailDTO {
    @NotBlank(message = "Username must be required")
    private String username;
    @NotBlank(message = "Email must be required")
    private String email;
    private String avatar;
    @JsonProperty("facebook_account")
    private String facebookAccountId;
    @JsonProperty("google_account")
    private String googleAccountId;
    @NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    private Long roleId;
    @NotBlank(message = "Password must be required")
    private String password;

    // Kiểm tra googleAccountId có hợp lệ không
    public boolean isGoogleAccountIdValid() {
        return googleAccountId != null && !googleAccountId.isEmpty();
    }

}
