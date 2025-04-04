package com.htt.elearning.user.controller;

import com.htt.elearning.user.dto.UserLoginMailDTO;
import com.htt.elearning.user.service.AuthService;
import com.htt.elearning.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/login")
public class LoginSocialController {
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/auth/social-login")
    public ResponseEntity<String> socialAuth(
            @RequestParam("login_type") String loginType,
            HttpServletRequest request
    ){
        //request.getRequestURI()
        loginType = loginType.trim().toLowerCase();  // Loại bỏ dấu cách và chuyển thành chữ thường
        String url = authService.generateAuthUrl(loginType);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/auth/social/callback")
    public ResponseEntity<?> callback(
            @RequestParam("code") String code,
            @RequestParam("login_type") String loginType,
            HttpServletRequest request
    ) throws Exception {
        Map<String, Object> userInfo = authService.authenticateAndFetchProfile(code, loginType);

        // Extract user information from userInfo map
        String accountId = "";
        String name = "";
        String email = "";
        String picture = "";

        if (loginType.trim().equals("google")) {
            accountId = (String) Objects.requireNonNullElse(userInfo.get("sub"), "");
            name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
            email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
            picture = (String) Objects.requireNonNullElse(userInfo.get("picture"), "");

        }
//        else if (loginType.trim().equals("facebook")) {
//            accountId = (String) Objects.requireNonNullElse(userInfo.get("id"), "");
//            name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
//            email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
//            // Lấy URL ảnh từ cấu trúc dữ liệu của Facebook
//            Object pictureObj = userInfo.get("picture");
//            if (pictureObj instanceof Map) {
//                Map<?, ?> pictureData = (Map<?, ?>) pictureObj;
//                Object dataObj = pictureData.get("data");
//                if (dataObj instanceof Map) {
//                    Map<?, ?> dataMap = (Map<?, ?>) dataObj;
//                    Object urlObj = dataMap.get("url");
//                    if (urlObj instanceof String) {
//                        picture = (String) urlObj;
//                    }
//                }
//            }
//        }

        UserLoginMailDTO userLoginMailDTO = UserLoginMailDTO.builder()
                .email(email)
                .username(name)
                .password("")
                .avatar(picture)
                .build();

        if (loginType.trim().equals("google")) {
            userLoginMailDTO.setGoogleAccountId(accountId);
            userLoginMailDTO.setFacebookAccountId("");
        }
//        else if (loginType.trim().equals("facebook")) {
//            userLoginMailDTO.setFacebookAccountId(accountId);
//            //userLoginDTO.setGoogleAccountId("");
//        }

//        String token = userService.loginSocial(userLoginMailDTO);
        return ResponseEntity.ok(userService.loginSocial(userLoginMailDTO));

//        return ResponseEntity.ok(userInfo);
    }
}
