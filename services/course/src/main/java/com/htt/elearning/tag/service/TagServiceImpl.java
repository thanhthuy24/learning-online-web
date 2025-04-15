package com.htt.elearning.tag.service;

import com.htt.elearning.tag.dto.TagDTO;
import com.htt.elearning.tag.pojo.Tag;
import com.htt.elearning.tag.repository.TagRepository;
import com.htt.elearning.tag.response.TagResponse;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public TagResponse getTagById(Long id) {
        Tag tag = tagRepository.findById(id).orElse(null);
        return modelMapper.map(tag, TagResponse.class);
    }

    @Override
    public List<TagResponse> getTagsByCourseId(List<Long> tagIds) {
        List<Tag> tags = tagRepository.findAllById(tagIds);
        return tags.stream()
                .map(TagResponse::fromTag)
                .collect(Collectors.toList());
    }

    @Override
    public Tag createTag(TagDTO tagDTO) {
        Tag newTag = Tag.builder()
                .name(tagDTO.getName())
                .build();
        return tagRepository.save(newTag);
    }
}
