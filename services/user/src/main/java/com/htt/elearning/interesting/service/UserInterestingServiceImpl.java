package com.htt.elearning.interesting.service;

import com.htt.elearning.category.CategoryClient;
import com.htt.elearning.category.response.CategoryResponse;
import com.htt.elearning.interesting.dto.UserInterestingDTO;
import com.htt.elearning.interesting.pojo.Userinteresting;
import com.htt.elearning.interesting.repository.UserInterestingRepository;
import com.htt.elearning.interesting.response.UserInterestingResponse;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import com.htt.elearning.user.pojo.User;
import com.htt.elearning.user.repository.UserRepository;
import com.htt.elearning.user.response.UserResponse;
import com.htt.elearning.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInterestingServiceImpl implements UserInterestingService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserInterestingRepository userInterestingRepository;
    private final CategoryClient categoryClient;
    private final HttpServletRequest request;

    @Override
    public List<UserInterestingResponse> getUserInteresting() {
        Long userId = userService.getUserIdByUsername();

        UserResponse userResponse = userService.getUserByUserIdClient(userId);

        List<Userinteresting> userInterestingList = userInterestingRepository.findByUserId(userId);
        List<Long> categoryIds = userInterestingList.stream()
                .map(Userinteresting::getCategoryId)
                .distinct()
                .collect(Collectors.toList());

        List<CategoryResponse> categories = categoryClient.getAllCategoriesByIds(categoryIds);
        Map<Long, CategoryResponse> categoryMap = categories.stream()
                .collect(Collectors.toMap(CategoryResponse::getId, Function.identity()));

        List<UserInterestingResponse> userInterestingResponses = userInterestingList.stream()
                .map(userinteresting -> {
                    CategoryResponse categoryResponse = categoryMap.get(userinteresting.getCategoryId());
                    return UserInterestingResponse.fromUserInteresting(userinteresting, userResponse, categoryResponse);
                })
                .collect(Collectors.toList());

        return userInterestingResponses;
    }

    @Override
    public List<Userinteresting> createUserInteresting(UserInterestingDTO userInterestingDTO) {
//        Long userId = userService.getUserIdByUsername();
        User user = userRepository.findById(userInterestingDTO.getUserId()).orElse(null);

        List<Userinteresting> savedList = new ArrayList<>();

        for (Long categoryId : userInterestingDTO.getCategoryIds()) {
            Userinteresting userInteresting = new Userinteresting();
            userInteresting.setUser(user);
            userInteresting.setCategoryId(categoryId);

            savedList.add(userInterestingRepository.save(userInteresting));
        }
        return savedList;

    }

    @Override
    public List<Long> getCategoryIdsByUserId() {
        Long userId = userService.getUserIdByUsername();

        return userInterestingRepository.getCategoryIdsByUserId(userId);
    }
}
