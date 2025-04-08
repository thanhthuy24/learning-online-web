package com.htt.elearning.cloudinary.controller;

import com.htt.elearning.cloudinary.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/cloudinary")
@RequiredArgsConstructor
public class CloudinaryController {
    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String imageUrl = cloudinaryService.uploadFile(file).toString();
        return ResponseEntity.ok(imageUrl);
    }

    @PostMapping("/upload-image")
    public Map<String, Object> uploadFileImage(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> imageUrl = cloudinaryService.uploadFile(file);
        return imageUrl;
    }
}
