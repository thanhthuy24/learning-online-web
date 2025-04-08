package com.htt.elearning.cloudinary;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(
        name = "cloudinary-service",
        url = "${application.config.cloudinary-url}"
)
public interface CloudinaryClient {
    @PostMapping("/upload")
    ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file);

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, Object> uploadFileImage(@RequestPart("file") MultipartFile file);
}
