package com.htt.elearning.overview.service;

import com.htt.elearning.overview.dto.UserOverviewDTO;
import com.htt.elearning.overview.pojo.Useroverview;
import com.htt.elearning.overview.response.UserOverviewResponse;

import java.util.List;

public interface UserOverviewService {
    UserOverviewResponse createOverview(UserOverviewDTO userOverviewDTO);
    UserOverviewResponse getOverviewByUserId(Long userId);
    List<Useroverview> getAllOverviews();

}
