package com.htt.elearning.user.service;

import com.htt.elearning.exception.DataNotFoundException;
import com.htt.elearning.exception.InvalidParamException;
import com.htt.elearning.user.dto.*;
import com.htt.elearning.user.pojo.User;
import com.htt.elearning.user.response.UserResponse;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    String login(String username, String password) throws DataNotFoundException, InvalidParamException;
    String loginSocial(UserLoginMailDTO userLoginMailDTO) throws InvalidParamException;

    User register(UserDTO userDTO) throws Exception;
    User getUserById(Long userId);
    User updateInformation(Long userId, UserUpdateDTO userUpdateDTO) throws Exception;
    Optional<User> getUserByUsername(String username);


    User updateAvatar(Long userId, UserUpdateDTO userUpdateDTO);
    void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) throws Exception;

    Page<UserResponse> getAllUsers(Pageable pageable, String keyword);
    Page<UserResponse> getUsersByRole(Long roleId, String key, Pageable pageable);

    UserResponse getUserByUserId(Long userId);
    User updateStatus(Long userId);

    Long getUserIdByUsername();

    User registerAccount(UserRegisterAccDTO userRegisterAccDTO) throws DataNotFoundException;

//    teacher - client
    UserResponse getUserByUsernameClient(String username);
    UserResponse getUserByUserIdClient(Long userId);
    UserResponse updateRole(Long userId);
    List<Long> searchUserIdsByKeyword(String keyword);

    List<UserResponse> getUsersByIds(List<Long> userIds);

    Page<UserResponse> getUsersTeachers(PageRequest pageRequest, String key);

    List<MonthStatDTO> getMonthlyGrowth(Long roleId);
}
