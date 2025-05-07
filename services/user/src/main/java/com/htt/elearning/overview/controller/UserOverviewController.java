package com.htt.elearning.overview.controller;

import com.htt.elearning.overview.dto.UserOverviewDTO;
import com.htt.elearning.overview.pojo.Useroverview;
import com.htt.elearning.overview.response.UserOverviewResponse;
import com.htt.elearning.overview.service.UserOverviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-overview")
public class UserOverviewController {
    private final UserOverviewService userOverviewService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserOverviewResponse> createUserOverview(
            @RequestBody UserOverviewDTO userOverviewDTO
    ) {
        return ResponseEntity.ok(userOverviewService.createOverview(userOverviewDTO));
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserOverviewResponse> getUserOverview(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userOverviewService.getOverviewByUserId(userId));
    }
}
