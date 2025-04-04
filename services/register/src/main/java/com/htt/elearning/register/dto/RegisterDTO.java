package com.htt.elearning.register.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    @Size(min = 2, max = 4000, message = "Reason must be between 2 and 4000 characters")
    private String reason;
    @Size(min = 2, max = 255, message = "Position must be between 2 and 255 characters")
    private String position;
    private Boolean status;
    @JsonProperty("user_id")
    private Long userId;
}
