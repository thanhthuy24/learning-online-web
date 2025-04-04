package com.htt.elearning.tag;

import com.htt.elearning.tag.response.TagResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "course-service",
        url = "${application.config.tag-url}"
)
public interface TagClient {
    @GetMapping("/{tagId}")
    TagResponse getTagById(@PathVariable Long tagId);
}
