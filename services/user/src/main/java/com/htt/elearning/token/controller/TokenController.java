package com.htt.elearning.token.controller;

import com.htt.elearning.exception.DataNotFoundException;
import com.htt.elearning.token.dto.TokenDTO;
import com.htt.elearning.token.pojo.Token;
import com.htt.elearning.token.response.TokenResponse;
import com.htt.elearning.token.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/token")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;
    private final HttpServletRequest request;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createToken(
            @RequestBody TokenDTO tokenDTO,
            BindingResult rs
    ) throws DataNotFoundException {
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        TokenResponse token = tokenService.createToken(tokenDTO);
        return ResponseEntity.ok(token);
    }

    //    xóa thông tin bằng token
    @PostMapping("/remove-token")
    public ResponseEntity<String> removeFcmToken() {
        String token = request.getHeader("Authorization");

        tokenService.removeFcmToken(token);
        return ResponseEntity.ok("FCM Token removed successfully");
    }

    //    xóa thông tin token bằng userID
    @DeleteMapping("/userId")
    public ResponseEntity<?> deleteToken(
            @PathVariable Long userId
    ){
        tokenService.removeTokensByUserId(userId);
        return ResponseEntity.ok("Token removed successfully");
    }

    @GetMapping("/get-list-tokens")
    public List<TokenResponse> getListTokens(
            @RequestParam List<Long> userIds
    ){
        return tokenService.getListTokens(userIds);
    }

}
