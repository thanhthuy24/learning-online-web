package com.htt.elearning.lesson.controller;

import com.htt.elearning.lesson.dtos.LessonDTO;
import com.htt.elearning.lesson.dtos.LessonVideoDTO;
import com.htt.elearning.lesson.pojo.Lesson;
import com.htt.elearning.lesson.response.LessonListResponse;
import com.htt.elearning.lesson.response.LessonResponse;
import com.htt.elearning.lesson.service.LessonService;
import com.htt.elearning.video.dtos.VideoDTO;
import com.htt.elearning.video.pojo.Video;
import com.htt.elearning.video.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/lessons")
@RequiredArgsConstructor
public class ApiLessonController {
    private final LessonService lessonService;
//    private final CloudinaryService cloudinaryService;
    private final VideoService videoService;
//    private final NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<LessonListResponse> getLessons(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
    ) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        // tao pageable tu thong tin page va limit
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by(direction, sortBy));
        Page<Lesson> lessonPage = lessonService.getAllLessons(keyword, pageRequest);

        // lay tong so trang
        int totalPage = lessonPage.getTotalPages();
        List<Lesson> lessons = lessonPage.getContent();
        return ResponseEntity.ok(LessonListResponse.builder()
                .lessons(lessons)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping("/auth/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(
            @PathVariable("lessonId") Long lessonId
    ) {
        Lesson lesson = lessonService.getLessonById(lessonId);
        return ResponseEntity.ok(lesson);
    }

    @PostMapping("")
    public ResponseEntity<?> createLesson(
            @Valid @ModelAttribute LessonDTO lessonDTO,
            BindingResult rs
    ) {
        try {
            if (rs.hasErrors()) {
                List<String> errorMessages = rs.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            Lesson newLesson = lessonService.createLesson(lessonDTO);

//            notificationService.sendNotificationToEnrolledUsers(
//                    lessonDTO.getCourseId(),
//                    "Bài học mới!",
//                    "Khóa học của bạn có bài học mới: " + lessonDTO.getName()
//            );

            return ResponseEntity.ok(newLesson);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping(value = "/uploads/{lessonId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadVideos(
            @PathVariable("lessonId") Long lessonId,
            @ModelAttribute("files") List<MultipartFile> files,
            @ModelAttribute("description") String description
    ) {
        try {
            Lesson existingLesson = lessonService.getLessonById(lessonId);
            files = files == null ? new ArrayList<>() : files;

            if(files.size() > Video.MAXIMUM_VIDEOS_PER_LESSON){
                return ResponseEntity.badRequest().body("You can upload only 5 videos");
            }

            List<Video> videos = new ArrayList<>();
            for(MultipartFile file : files) {
                if(file.isEmpty()) {
                    continue;
                }
                if(file.getSize() > 20 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }

                // tai file len cloudinary va lay URl
                String videoUrl = storeFile(file);

                //luu vao doi tuong
                Video video = lessonService.createVideo(
                        existingLesson.getId(),
                        VideoDTO.builder()
                                .name(videoUrl)
                                .description(description)
                                .build()
                );
                videos.add(video);
            }
            return ResponseEntity.ok(videos);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) /*throws IOException*/ {
        // tai file len cloudinary va lay URl
//        Map<String, Object> uploadResult = cloudinaryService.uploadFile(file);
//        return uploadResult.get("url").toString();
        return null;
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<?> updateLesson(
            @PathVariable Long lessonId,
            @Valid @ModelAttribute LessonDTO lessonDTO,
            BindingResult rs
    ) {
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        lessonService.updateLesson(lessonId, lessonDTO);
        return ResponseEntity.ok(lessonDTO);
    }

    @PutMapping("/update-video/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateVideo(
            @PathVariable Long videoId,
            @ModelAttribute VideoDTO videoDTO,
            BindingResult rs
    ){
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        videoService.updateVideo(videoId, videoDTO);
        return ResponseEntity.ok(videoDTO);
    }

    @PutMapping("/{lessonId}/active")
    public ResponseEntity<?> updateActiveLesson(
            @PathVariable Long lessonId,
            @Valid @ModelAttribute LessonDTO lessonDTO,
            BindingResult rs
    ) {
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Lesson updatedLesson = lessonService.updateActiveLesson(lessonId, lessonDTO);
        if (updatedLesson == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found with id: " + lessonId);
        }

        return ResponseEntity.ok(lessonDTO);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<String> deleteLesson(
            @PathVariable Long lessonId
    ) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.ok("delete lesson success");
    }

    @GetMapping("/auth/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LessonVideoDTO>> getLessonsByCourseId(
            @PathVariable("courseId") Long courseId
    ) {
        List<LessonVideoDTO> listLesson = lessonService.getLessonByCourseId(courseId);
        return ResponseEntity.ok(listLesson);
    }

    @GetMapping("/count-by-course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> countLessonsByCourseId(
            @PathVariable Long courseId
    ){
        return ResponseEntity.ok(lessonService.countLessonInCourse(courseId));
    }

    @GetMapping("/get-first-lesson/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getFirstLessonByCourseId(
            @PathVariable Long courseId
    ){
        return ResponseEntity.ok(lessonService.getFirstLesson(courseId));
    }

//    lesson - client
    @GetMapping("/get-lesson/{lessonId}")
    @ResponseStatus(HttpStatus.OK)
    public LessonResponse getLessonByIdClient(
            @PathVariable Long lessonId
    ) {
        return lessonService.getLessonByIdClient(lessonId);
    }

    @GetMapping("/get-list-lessons/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public List<LessonResponse> getListLessonsByCourseIdClient(
            @PathVariable Long courseId
    ) {
        return lessonService.getLessonsByCourseIdClient(courseId);
    }
}
