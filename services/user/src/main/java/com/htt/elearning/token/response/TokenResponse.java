package com.htt.elearning.token.response;

import com.htt.elearning.token.pojo.Token;
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

    public static TokenResponse fromToken(Token token) {
        TokenResponse tokenResponse = TokenResponse.builder()
                .id(token.getId())
                .token(token.getToken())
                .userId(token.getUser().getId())
                .build();

        return tokenResponse;
    }
}
