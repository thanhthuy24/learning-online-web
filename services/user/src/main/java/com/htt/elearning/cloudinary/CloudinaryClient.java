package com.htt.elearning.cloudinary;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "cloudinary-service",
        url = "${application.config.cloudinary-url}"
)
public interface CloudinaryClient {
    @PostMapping("api/cloudinary/upload")
    ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file);
}
