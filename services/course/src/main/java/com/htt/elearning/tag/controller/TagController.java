package com.htt.elearning.tag.controller;

import com.htt.elearning.tag.dto.TagDTO;
import com.htt.elearning.tag.pojo.Tag;
import com.htt.elearning.tag.response.TagResponse;
import com.htt.elearning.tag.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> getAllTags = tagService.getAllTags();
        return ResponseEntity.ok(getAllTags);
    }

    @GetMapping("/{tagId}")
    public TagResponse getTagById(@PathVariable Long tagId) {
        return tagService.getTagById(tagId);
    }

    @PostMapping("")
    public ResponseEntity<?> createTag(
            @Valid
            @RequestBody TagDTO tagDTO,
            BindingResult rs) {
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        tagService.createTag(tagDTO);
        return ResponseEntity.ok("insert category successfully!");
    }
}
