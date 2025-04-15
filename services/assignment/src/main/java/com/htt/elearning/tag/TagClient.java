package com.htt.elearning.tag;

import com.htt.elearning.tag.response.TagResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "course-service",
        contextId = "courseServiceV3",
        url = "${application.config.tag-url}"
)
public interface TagClient {
    @GetMapping("/{tagId}")
    TagResponse getTagById(@PathVariable Long tagId);

    @GetMapping("/get-tags")
    ResponseEntity<List<TagResponse>> getTagsByIds(
            @RequestParam List<Long> tagIds,
            @RequestHeader("Authorization") String token);
}
