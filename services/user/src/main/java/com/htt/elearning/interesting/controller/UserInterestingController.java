package com.htt.elearning.interesting.controller;

import com.htt.elearning.interesting.dto.UserInterestingDTO;
import com.htt.elearning.interesting.pojo.Userinteresting;
import com.htt.elearning.interesting.response.UserInterestingResponse;
import com.htt.elearning.interesting.service.UserInterestingService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-interesting")
public class UserInterestingController {
    private final UserInterestingService userInterestingService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserInterestingResponse> getUserInteresting() {
        return userInterestingService.getUserInteresting();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Userinteresting> createUserInteresting(
            @RequestBody UserInterestingDTO userInterestingDTO
    ) {
        return userInterestingService.createUserInteresting(userInterestingDTO);
    }

    @GetMapping("/get-list-categories")
    @ResponseStatus(HttpStatus.OK)
    public List<Long>  getCategoryIdsByUserId() {
        return userInterestingService.getCategoryIdsByUserId();
    }
}
