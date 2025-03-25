package com.htt.elearning.tag.service;

import com.htt.elearning.tag.dto.TagDTO;
import com.htt.elearning.tag.pojo.Tag;
import com.htt.elearning.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public Tag createTag(TagDTO tagDTO) {
        Tag newTag = Tag.builder()
                .name(tagDTO.getName())
                .build();
        return tagRepository.save(newTag);
    }
}
