package com.htt.elearning.token.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TokenResponse {
    private Long id;
    private String token;
    private Long userId;
}
