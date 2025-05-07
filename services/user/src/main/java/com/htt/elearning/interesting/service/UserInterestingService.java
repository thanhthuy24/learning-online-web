package com.htt.elearning.interesting.service;

import com.htt.elearning.interesting.dto.UserInterestingDTO;
import com.htt.elearning.interesting.pojo.Userinteresting;
import com.htt.elearning.interesting.response.UserInterestingResponse;

import java.util.List;

public interface UserInterestingService {
    List<UserInterestingResponse> getUserInteresting();
    List<Userinteresting> createUserInteresting(UserInterestingDTO userInterestingDTO);

    List<Long> getCategoryIdsByUserId();
}
