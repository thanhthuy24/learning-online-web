package com.htt.elearning.tag.service;

import com.htt.elearning.tag.dto.TagDTO;
import com.htt.elearning.tag.pojo.Tag;
import com.htt.elearning.tag.response.TagResponse;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();
    TagResponse getTagById(Long id);
    Tag createTag(TagDTO tagDTO);
    List<TagResponse> getTagsByCourseId(List<Long> tagIds);
}
