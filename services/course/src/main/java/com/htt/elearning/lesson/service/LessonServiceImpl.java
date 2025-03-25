package com.htt.elearning.lesson.service;

import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.repository.CourseRepository;
import com.htt.elearning.lesson.dtos.LessonDTO;
import com.htt.elearning.lesson.dtos.LessonVideoDTO;
import com.htt.elearning.lesson.dtos.LessonVideoIntro;
import com.htt.elearning.lesson.exceptions.InvalidParamException;
import com.htt.elearning.lesson.pojo.Lesson;
import com.htt.elearning.lesson.repository.LessonRepository;
import com.htt.elearning.video.dtos.VideoDTO;
import com.htt.elearning.video.pojo.Video;
import com.htt.elearning.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final VideoRepository videoRepository;
//    private final UserRepository userRepository;
//    private final EnrollmentRepository enrollmentRepository;
//    private final TeacherRepository teacherRepository;
//    private final NotificationRepository notificationRepository;

    @Override
    public Lesson createLesson(LessonDTO lessonDTO) {
        Course existCourse = courseRepository
                .findById(lessonDTO.getCourseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Can not find course by id " + lessonDTO.getCourseId()));

        Lesson newLesson = Lesson.builder()
                .name(lessonDTO.getName())
                .description(lessonDTO.getDescription())
                .course(existCourse)
                .build();

//        List<User> users = enrollmentRepository.findUsersByCourseId(lessonDTO.getCourseId());
//        List<Notification> notifications = users.stream()
//                .map(user -> Notification.builder()
//                        .title("Khoá học " + existCourse.getName() + " bạn đang ký vừa có bài học mới!")
//                        .message("Bài học mới: " + lessonDTO.getName() + ", hãy check ngay nào, " + user.getUsername() + " ơi!")
//                        .user(user)
//                        .isRead(false)
//                        .createdDate(new Date())
//                        .build()
//                )
//                .collect(Collectors.toList());
//        notificationRepository.saveAll(notifications);

        return lessonRepository.save(newLesson);
    }

    @Override
    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find lesson by id " + id));
    }

    @Override
    public Page<Lesson> getAllLessons(String keyword, PageRequest pageRequest) {
        return lessonRepository
                .searchLessonsAll(keyword, pageRequest);
//        return lessonRepository.findAll(pageRequest);
    }

    @Override
    public Lesson updateLesson(Long id, LessonDTO lessonDTO) {
        Lesson existingLesson = getLessonById(id);
        if (existingLesson != null) {
            Course existCourse = courseRepository.findById(lessonDTO.getCourseId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find course by id " + id));
            existingLesson.setName(lessonDTO.getName());
            existingLesson.setDescription(lessonDTO.getDescription());
            existingLesson.setCourse(existCourse);
            return lessonRepository.save(existingLesson);
        }
        return null;
    }

    @Override
    public Lesson updateActiveLesson(Long id, LessonDTO lessonDTO){
        Lesson existingLesson = getLessonById(id);
        if (existingLesson != null) {
            Course existCouse = courseRepository.findById(lessonDTO.getCourseId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Cannot find course by id " + id));
            existingLesson.setName(lessonDTO.getName());
            existingLesson.setDescription(lessonDTO.getDescription());
            existingLesson.setCourse(existCouse);
            existingLesson.setIsActive(lessonDTO.getIsActive());
            return lessonRepository.save(existingLesson);
        }
        return null;
    }

    @Override
    public void deleteLesson(Long id) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(id);

        Lesson lesson = optionalLesson.get();

        videoRepository.deleteAll(lesson.getVideos());

        lessonRepository.delete(lesson);
    }

    @Override
    public boolean existByName(String name) {
        return lessonRepository.existsByName(name);
    }

    @Override
    public LessonVideoIntro getFirstLesson(Long courseId) {
        Lesson lesson = lessonRepository.findFirstLesson(courseId);
        if (lesson == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find lesson by id " + courseId);
        }
        return converToIntro(lesson);
    }

    @Override
    public List<LessonVideoDTO> getLessonByCourseId(Long courseId) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Long role = userRepository.getUserByUsername(username).getRole().getId();
//        Long userId = userRepository.getUserByUsername(username).getId();
//
//        if(role == 1) {
//            Optional<Enrollment> checkEnrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
//            if (checkEnrollment.isEmpty()) {
//                throw new ResponseStatusException(
//                        HttpStatus.FORBIDDEN, "You must enroll this course first!!"
//                );
//            }
//        }
//        Course course = courseRepository.getCourseById(courseId);
//        Long ownerCourseId = course.getTeacher().getId();
//        Teacher teacher = teacherRepository.findByUserId(userId);
//
//        if(role == 3) {
//            Long teacherId = teacher.getId();
//            if (ownerCourseId != teacherId) {
//                throw new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "You don't have permission to access this course!!"
//                );
//            }
//        }
//
//        List<Lesson> lessons = lessonRepository.findByCourseId(courseId);
//        return lessons.stream().map(this::convertToDTO).collect(Collectors.toList());
        return null;
    }

    private LessonVideoDTO convertToDTO(Lesson lesson) {
        // Xây dựng danh sách VideoDTO bằng builder
        List<Video> videos = lesson.getVideos().stream()
                .map(video -> Video.builder()
                        .id(video.getId())
                        .name(video.getName())
                        .description(video.getDescription())
                        .lesson(video.getLesson())
                        .build())
                .collect(Collectors.toList());

        // Sử dụng builder để tạo LessonDTO
        return LessonVideoDTO.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .description(lesson.getDescription())
                .videos(videos)
//                .course(course)
                .build();
    }

    private LessonVideoIntro converToIntro(Lesson lesson) {
        List<Video> videos = lesson.getVideos().stream()
                .map(video -> Video.builder()
                        .id(video.getId())
                        .name(video.getName())
                        .description(video.getDescription())
                        .build())
                .collect(Collectors.toList());

        return LessonVideoIntro.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .description(lesson.getDescription())
                .videos(videos)
                .build();
    }

    @Override
    public Video createVideo(
            Long lessonId,
            VideoDTO videoDTO) throws InvalidParamException {
        Lesson existingLesson = lessonRepository
                .findById(lessonId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find Lesson with id " + videoDTO.getLessonId()));

        Video newVideo = Video.builder()
                .name(videoDTO.getName())
                .description(videoDTO.getDescription())
                .lesson(existingLesson)
                .build();

        //khong cho insert qua 5 video trong 1 lesson
        int size = videoRepository.findByLessonId(lessonId).size();
        if (size >= Video.MAXIMUM_VIDEOS_PER_LESSON) {
            throw new InvalidParamException("Number of videos must be <= " + Video.MAXIMUM_VIDEOS_PER_LESSON);
        }

        return videoRepository.save(newVideo);
    }

    @Override
    public Long countLessonInCourse(Long courseId) {
        Long number = lessonRepository.countLessonsByCourseId(courseId);
        if (number != null) {
            return number;
        }
        return 0L;
    }
}
