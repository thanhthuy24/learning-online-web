package com.htt.elearning.token.service;

import com.htt.elearning.exception.DataNotFoundException;
import com.htt.elearning.token.dto.TokenDTO;
import com.htt.elearning.token.pojo.Token;
import com.htt.elearning.token.repository.TokenRepository;
import com.htt.elearning.token.response.TokenResponse;
import com.htt.elearning.user.pojo.User;
import com.htt.elearning.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    public final TokenRepository tokenRepository;
    public final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public TokenResponse createToken(TokenDTO tokenDTO) throws DataNotFoundException {
        User existingUser = userRepository.findById(tokenDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!!"));

        Token existingToken = tokenRepository.findByTokenAndUserId(tokenDTO.getToken(), existingUser.getId());
        if (existingToken != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Token already exists");
        }

        Token newToken = Token.builder()
                .token(tokenDTO.getToken())
                .user(existingUser)
                .build();
        tokenRepository.save(newToken);

        return TokenResponse.fromToken(newToken);
    }

    @Override
    public void saveToken(String token, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!!"));

        Optional<Token> existingToken = tokenRepository.findByToken(token);
        if (existingToken.isPresent()) {
            return;
        }

        Token fcmToken  = new Token();
        fcmToken .setToken(token);
        fcmToken .setUser(user);
        tokenRepository.save(fcmToken );

    }

    @Override
    public void removeTokensByUserId(Long userId) {
        tokenRepository.deleteByUserId(userId);
    }

    @Override
    public void removeFcmToken(
            @RequestHeader("Authorization") String token
    ) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Cắt bỏ "Bearer "
        }
        tokenRepository.findByToken(token).ifPresent(tokenRepository::delete);
    }

    @Override
    public List<TokenResponse> getListTokens(List<Long> userIds) {
        List<Token> tokens = tokenRepository.findByUserIdIn(userIds);
        return modelMapper.map(tokens, new TypeToken<List<TokenResponse>>() {}.getType());
    }
}
