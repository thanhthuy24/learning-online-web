package com.htt.elearning.user.controller;

import com.htt.elearning.cloudinary.CloudinaryClient;
import com.htt.elearning.user.dto.*;
import com.htt.elearning.user.pojo.User;
import com.htt.elearning.user.response.UserListResponse;
import com.htt.elearning.user.response.UserResponse;
import com.htt.elearning.user.service.AuthService;
import com.htt.elearning.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CloudinaryClient cloudinaryService;
    private final AuthService authService;

    @GetMapping("/all-users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllUsers(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
    ) {
        Sort.Direction direction = order.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        // tao pageable tu thong tin page va limit
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by(direction, sortBy));
        Page<UserResponse> userPage = userService.getAllUsers(pageRequest, keyword);

        // lay tong so trang
        int totalPage = userPage.getTotalPages();
        List<UserResponse> users = userPage.getContent();
        return ResponseEntity.ok(UserListResponse.builder()
                .users(users)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping("/get-users/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getStudents(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(value = "key", required = false, defaultValue = "") String key,
            @PathVariable Long roleId
    ) {
        Sort.Direction direction = order.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        // tao pageable tu thong tin page va limit
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by(direction, sortBy));
        Page<UserResponse> userPage = userService.getUsersByRole(roleId, key, pageRequest);

        // lay tong so trang
        int totalPage = userPage.getTotalPages();
        List<UserResponse> users = userPage.getContent();
        return ResponseEntity.ok(UserListResponse.builder()
                .users(users)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping(path = "/current-user", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<User> details(Principal user) {
        Optional<User> u = this.userService.getUserByUsername(user.getName());
        return u.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createUser(
            @Valid @ModelAttribute UserDTO userDTO,
            BindingResult rs
    ) {
        try{
            if(rs.hasErrors()){
                List<String> errorMessages = rs.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Passwords do not match");
            }

            MultipartFile file = userDTO.getFile();
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is missing or empty");
            }

            //kiem tra kích thuớc và định dạng file ảnh
            if (file.getSize() > 10 * 1024 * 1024)
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File is too large, Maximum is 10MB");

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("File must be an image");
            }

            String imageUrl = storeFile(file);

            // Thiết lập URL của hình ảnh cho CourseDTO
            userDTO.setAvatar(imageUrl);

            User user = userService.register(userDTO);
            return ResponseEntity.ok(user);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/register-account", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerAccount(
            @Valid @ModelAttribute UserRegisterAccDTO userRegisterAccDTO,
            BindingResult rs
    ) {
        try{
            if(rs.hasErrors()){
                List<String> errorMessages = rs.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userRegisterAccDTO.getPassword().equals(userRegisterAccDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Passwords do not match");
            }
            User user = userService.registerAccount(userRegisterAccDTO);
            return ResponseEntity.ok(user);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    private String storeFile(MultipartFile file) throws IOException {
//        Map<String, Object> uploadResult = cloudinaryService.uploadFile(file);
//        return uploadResult.get("url").toString();
//    }

    private String storeFile(MultipartFile file) {
        try {
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinaryService.uploadFile(file);
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi upload file lên Cloudinary", e);
        }
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new LoginResponseDTO("Login failed: " + e.getMessage()));
        }
        //kiểm tra thông tin đăng nhập và token
//        try {
//            String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
//            return ResponseEntity.ok(token);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
        // trả về token trong response

    }

    @PutMapping("/update-user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> updateInformation(
            @PathVariable("userId") Long userId,
            @Valid @ModelAttribute UserUpdateDTO userUpdateDTO,
            BindingResult rs
    ) throws Exception {
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        userService.updateInformation(userId, userUpdateDTO);
        return ResponseEntity.ok(userUpdateDTO);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getUser(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }

    @GetMapping("/get-userId")
    @ResponseStatus(HttpStatus.OK)
    public Long getUserIdByUsernameClient() {
        return userService.getUserIdByUsername();
    }

    @PatchMapping("/update-avatar/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateAvatar(
            @PathVariable("userId") Long userId,
            @Valid @ModelAttribute UserUpdateDTO userUpdateDTO
    ) throws IOException {
        MultipartFile file = userUpdateDTO.getFile();
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is missing or empty");
        }

        //kiem tra kích thuớc và định dạng file ảnh
        if (file.getSize() > 10 * 1024 * 1024)
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("File is too large, Maximum is 10MB");

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("File must be an image");
        }

        String imageUrl = storeFile(file);

        // Thiết lập URL của hình ảnh cho CourseDTO
        userUpdateDTO.setAvatar(imageUrl);
        return ResponseEntity.ok(userService.updateAvatar(userId, userUpdateDTO));
    }

    @PatchMapping("/change-password/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO
    ) throws Exception {
        userService.changePassword(userId, changePasswordDTO);
        return ResponseEntity.ok("Mật khẩu đã được thay đổi thành công");
    }

    @PatchMapping("/update-active/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> updateUserActive(
            @PathVariable("userId") Long userId
    ){
        userService.updateStatus(userId);
        return ResponseEntity.ok("Successfull");
    }

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

        if (loginType.trim().equals("google")) {
            accountId = (String) Objects.requireNonNullElse(userInfo.get("sub"), "");
            name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
            email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
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
        return ResponseEntity.ok(userInfo);
    }

//    user - client
    @GetMapping(path = "/get-current-user")
    public ResponseEntity<UserResponse> getCurrentUserClient() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.getUserByUsernameClient(username));
    }

    @GetMapping("/get-username")
    @ResponseStatus(HttpStatus.OK)
    public String getUsernameClient(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username;
    }

    @GetMapping("/get-role-id")
    @ResponseStatus(HttpStatus.OK)
    public Long getRoleIdClient(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long roleId = userService.getUserByUsernameClient(username).getRoleId();
        return roleId;
    }

    @GetMapping("/get-user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserByIdClient(
            @PathVariable("userId") Long userId
    ) {
        return userService.getUserByUserIdClient(userId);
    }

    @GetMapping("/update-role-teacher/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateRoleTeacher(
            @PathVariable("userId") Long userId
    ){
        return userService.updateRole(userId);
    }

    @GetMapping("/get-users-keyword")
    public List<Long> searchUserIdsByKeywordClient(
            @RequestParam("keyword") String keyword
    ){
        return userService.searchUserIdsByKeyword(keyword);
    }
}
