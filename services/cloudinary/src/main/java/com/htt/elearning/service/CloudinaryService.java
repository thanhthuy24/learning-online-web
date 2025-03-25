package com.htt.elearning.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public Map uploadFile(MultipartFile file) throws IOException {
        File fileToUpload = convert(file);
//        Map uploadResult = cloudinary.uploader().upload(fileToUpload, ObjectUtils.emptyMap());
        Map uploadResult = cloudinary.uploader().upload(fileToUpload, ObjectUtils.asMap("resource_type", "auto"));

        fileToUpload.delete();
        return uploadResult;
    }

    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename());

        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;

    }
}
