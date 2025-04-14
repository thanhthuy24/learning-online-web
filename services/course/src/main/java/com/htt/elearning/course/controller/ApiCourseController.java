package com.htt.elearning.course.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.htt.elearning.cloudinary.CloudinaryClient;
import com.htt.elearning.course.dto.CourseDTO;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.response.*;
import com.htt.elearning.course.service.CourseRedisService;
import com.htt.elearning.course.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class ApiCourseController {
    private final CloudinaryClient cloudinaryClient;
    private final CourseService courseService;
    private static final Logger logger = LoggerFactory.getLogger(ApiCourseController.class);
    private final CourseRedisService courseRedisService;
    private final HttpServletRequest request;

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
    public ResponseEntity<TestCourseResponse> getCourseById(
            @PathVariable("courseId") Long courseId
    ) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @GetMapping("/teacher/{teacherId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCourseByTeacherId(
            @PathVariable("teacherId") Long teacherId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
//        return ResponseEntity.ok(courseService.getCoursesByTeacherId(teacherId));
        Pageable pageable = PageRequest.of(page, limit,
                Sort.by("createdDate").descending());
        Page<Course> coursePage = courseService.getCoursesByTeacherId(teacherId, pageable);
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
        String token = request.getHeader("Authorization");
        Map<String, Object> uploadResult = cloudinaryClient.uploadFileImage(file, token);
        return uploadResult.get("url").toString();
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

    @GetMapping("/course-teacher/{teacherId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCoursesByTeacher(
            @PathVariable("teacherId") Long teacherId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("createdDate").descending());
        Page<TestCourseResponse> coursePage = courseService.getTestCoursesByTeacherId(teacherId, pageRequest);
        int totalPage = coursePage.getTotalPages();
        List<TestCourseResponse> courses = coursePage.getContent();
        return ResponseEntity.ok(TestList.builder()
                .courses(courses)
                .totalPages(totalPage)
                .build());
    }

//    course - client
    @GetMapping("/get-courseId/{courseId}")
    public CourseResponse getCourseByIdClient(
            @PathVariable Long courseId
    ) {
        return courseService.getCourseByIdClient(courseId);
    }

    @GetMapping("/get-courses-keyword")
    public List<Long> getCoursesByKeywordClient(String keyword) {
        return courseService.searchCourseIdsByNameClient(keyword);
    }

    @GetMapping("/get-all-courses-redis")
    public ResponseEntity<CourseResponseRedisList> getAllCourseRedis(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) throws JsonProcessingException {
        int totalPages = 0;
        courseRedisService.clear();
        // Tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").descending()
        );
        logger.info(String.format("keyword = %s, category_id = %d, page = %d, limit = %d",
                keyword, categoryId, page, limit));
        List<CourseResponseRedis> courseResponseRedis = courseRedisService
                .getAllCourses(pageRequest, categoryId, keyword);
        if (courseResponseRedis!=null && !courseResponseRedis.isEmpty()) {
            totalPages = courseResponseRedis.get(0).getTotalPages();
        }
        if(courseResponseRedis == null) {
            Page<CourseResponseRedis> coursePage = courseService
                    .getAllCoursesRedisClient(pageRequest, keyword, categoryId);
            // Lấy tổng số trang
            totalPages = coursePage.getTotalPages();
            courseResponseRedis = coursePage.getContent();
            // Bổ sung totalPages vào các đối tượng ProductResponse
            for (CourseResponseRedis course : courseResponseRedis) {
                course.setTotalPages(totalPages);
            }
            courseRedisService.saveAllCourses(
                    courseResponseRedis,
                    pageRequest,
                    categoryId,
                    keyword
            );
        }
        CourseResponseRedisList courseResponseRedisList = CourseResponseRedisList
                .builder()
                .courses(courseResponseRedis)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(courseResponseRedisList);
    }

    @GetMapping("/test-courses")
    public ResponseEntity<TestList> testCourses(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        Pageable pageRequest = PageRequest.of(page, limit,
                Sort.by("createdDate").descending());
        Page<TestCourseResponse> coursePage = courseService.testCourses(pageRequest);

        // lay tong so trang
        int totalPage = coursePage.getTotalPages();
        List<TestCourseResponse> courses = coursePage.getContent();
        return ResponseEntity.ok(TestList.builder()
                .courses(courses)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping("/get-course-by-id")
    public TestCourseResponse getFullCourseResponse(
            @PathVariable Long courseId
    ){
        TestCourseResponse testCourseResponse = courseService.getTestCourseResponseByCourseId(courseId);
        return testCourseResponse;
    }

    @GetMapping("/get-course-by-ids")
    public List<TestCourseResponse> getFullCourseResponses(
            @RequestParam List<Long> courseIds
    ){
        return courseService.getTestCourseResponseByCourseIds(courseIds);
    }

}
