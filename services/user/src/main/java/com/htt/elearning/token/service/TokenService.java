package com.htt.elearning.token.service;

import com.htt.elearning.exception.DataNotFoundException;
import com.htt.elearning.token.dto.TokenDTO;
import com.htt.elearning.token.pojo.Token;
import com.htt.elearning.token.response.TokenResponse;

import java.util.List;

public interface TokenService {
    TokenResponse createToken(TokenDTO tokenDTO) throws DataNotFoundException;
    void saveToken(String token, Long userId);
    void removeTokensByUserId(Long userId);
    void removeFcmToken(String token);

//    token - client
    List<TokenResponse> getListTokens(List<Long> userIds);
}
