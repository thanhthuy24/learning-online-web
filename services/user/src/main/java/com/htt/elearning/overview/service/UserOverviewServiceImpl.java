package com.htt.elearning.overview.service;

import com.htt.elearning.overview.dto.UserOverviewDTO;
import com.htt.elearning.overview.pojo.Useroverview;
import com.htt.elearning.overview.repository.UserOverviewRepository;
import com.htt.elearning.overview.response.UserOverviewResponse;
import com.htt.elearning.user.pojo.User;
import com.htt.elearning.user.repository.UserRepository;
import com.htt.elearning.user.response.UserResponse;
import com.htt.elearning.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOverviewServiceImpl implements UserOverviewService {
    private final UserOverviewRepository userOverviewRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public UserOverviewResponse createOverview(UserOverviewDTO userOverviewDTO) {
//        Long userId = userService.getUserIdByUsername();
        User user = userRepository.findById(userOverviewDTO.getUserId()).orElse(null);

        UserResponse userResponse = userService.getUserByUserId(userOverviewDTO.getUserId());

        Useroverview userOverview = Useroverview.builder()
                .job(userOverviewDTO.getJob())
                .gender(userOverviewDTO.getGender())
                .tag(userOverviewDTO.getTagId())
                .user(user)
                .dailyHours(userOverviewDTO.getDailyHours())
                .build();

        userOverviewRepository.save(userOverview);
        return UserOverviewResponse.fromUserOverview(userOverview, userResponse);
    }

    @Override
    public UserOverviewResponse getOverviewByUserId(Long userId) {
        Long userIds = userService.getUserIdByUsername();
        UserResponse userResponse = userService.getUserByUserId(userIds);

        Useroverview useroverview = userOverviewRepository.findByUserId(userIds);

        return UserOverviewResponse.fromUserOverview(useroverview, userResponse);

    }

    @Override
    public List<Useroverview> getAllOverviews() {
        return userOverviewRepository.findAll();
    }
}
