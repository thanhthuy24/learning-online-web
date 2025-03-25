package com.htt.elearning.course.controller;

import com.htt.elearning.course.dto.CourseDTO;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.response.CourseListResponse;
import com.htt.elearning.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/courses")
@RequiredArgsConstructor
public class ApiCourseController {
//    private final CloudinaryService cloudinaryService;
    private final CourseService courseService;
    //hien thi tat ca courses
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CourseListResponse> getAllCourses(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        // tao pageable tu thong tin page va limit
        Pageable pageRequest = PageRequest.of(page, limit,
                Sort.by("createdDate").descending());
        Page<Course> coursePage = courseService.getAllCourses(pageRequest);

        // lay tong so trang
        int totalPage = coursePage.getTotalPages();
        List<Course> courses = coursePage.getContent();
        return ResponseEntity.ok(CourseListResponse.builder()
                .courses(courses)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping("/filter-cate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CourseListResponse> getCoursesByCategoryId(
            @RequestParam(required = false) Long categoryId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        Pageable pageRequest = PageRequest.of(page, limit,
                Sort.by("createdDate").descending());
        Page<Course> coursePage = courseService.getCoursesByCategoryIdPage(pageRequest, categoryId);
        int totalPage = coursePage.getTotalPages();
        List<Course> courses = coursePage.getContent();
        return ResponseEntity.ok(CourseListResponse.builder()
                .courses(courses)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping("/filter-price")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CourseListResponse> getCoursesByPrice(
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        Pageable pageRequest = PageRequest.of(page, limit,
                Sort.by("createdDate").descending());
        Page<Course> coursePage = courseService.getCoursesByPrice(minPrice, maxPrice, pageRequest);
        int totalPage = coursePage.getTotalPages();
        List<Course> courses = coursePage.getContent();
        return ResponseEntity.ok(CourseListResponse.builder()
                .courses(courses)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping("/search")
    public List<Course> searchCourses(
            @RequestParam String keyword
    ) {
        return courseService.searchCourses(keyword);
    }

    @GetMapping("/name-course")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllCourseName(){
        return ResponseEntity.ok(courseService.getAllCourseName());
    }

    @GetMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Course> getCourseById(
            @PathVariable("courseId") Long courseId
    ) {
        Course courseById = courseService.getCourseById(courseId);
        return ResponseEntity.ok(courseById);
    }

    @GetMapping("/teacher/{teacherId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCourseByTeacherId(
            @PathVariable("teacherId") Long teacherId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
//        return ResponseEntity.ok(courseService.getCoursesByTeacherId(teacherId));
        Pageable pageRequest = PageRequest.of(page, limit,
                Sort.by("createdDate").descending());
        Page<Course> coursePage = courseService.getCoursesByTeacherId(teacherId, pageRequest);
        int totalPage = coursePage.getTotalPages();
        List<Course> courses = coursePage.getContent();
        return ResponseEntity.ok(CourseListResponse.builder()
                .courses(courses)
                .totalPages(totalPage)
                .build());
    }


    //them category
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCourse(
            @Valid @ModelAttribute CourseDTO courseDTO,
            BindingResult rs
    ) throws IOException {
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }

        MultipartFile file = courseDTO.getFile();
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is missing or empty");
        }

        //kiem tra kích thuớc và định dạng file ảnh
        if (file.getSize() > 10 * 1024 * 1024)
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("File is too large, Maximum is 10MB");

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("File must be an image");
        }

        String imageUrl = storeFile(file);

        // Thiết lập URL của hình ảnh cho CourseDTO
        courseDTO.setImage(imageUrl);
        courseService.createCourse(courseDTO);
        return ResponseEntity.ok(courseDTO);
    }

    private String storeFile(MultipartFile file) throws IOException {
//        Map<String, Object> uploadResult = cloudinaryService.uploadFile(file);
//        return uploadResult.get("url").toString();
        return null;
    }

    @PutMapping("/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> updateCourse(
            @PathVariable Long courseId,
            @Valid @ModelAttribute CourseDTO courseDTO,
            BindingResult rs
    ) {
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        courseService.updateCourse(courseId, courseDTO);
        return ResponseEntity.ok(courseDTO);
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok("delete course");
    }
}
