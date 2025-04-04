package com.htt.elearning.progress.service;

import com.htt.elearning.course.CourseClient;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.enrollment.pojo.Enrollment;
import com.htt.elearning.enrollment.repository.EnrollmentRepository;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.lesson.LessonClient;
import com.htt.elearning.lesson.response.LessonResponse;
import com.htt.elearning.progress.pojo.Progress;
import com.htt.elearning.progress.repository.ProgressRepository;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.video.VideoClient;
import com.htt.elearning.videocompleted.VideoCompletedClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepository;
    private final VideoClient videoClient;
    private final VideoCompletedClient videoCompletedClient;
    private final UserClient userClient;
    private final LessonClient lessonClient;
    private final CourseClient courseClient;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public float calculateProgress(Long courseId) {
        List<LessonResponse> lessonList = lessonClient.getListLessonsByCourseIdClient(courseId);

        if (lessonList.isEmpty())
            return 0f;

        Long userId = userClient.getUserIdByUsername();

        Long totalVideoByLesson = 0L;
        Long totalVideoCompleted = 0L;
        for (LessonResponse lesson : lessonList) {
            Long videoCount = videoClient.getLessonCountClient(lesson.getId());
            Long videoCompleted = videoCompletedClient.countVideoCompletedBy(lesson.getId());
            totalVideoByLesson += videoCount;
            totalVideoCompleted += videoCompleted;
        }

        Float progress;
        if(totalVideoByLesson > 0) {
            progress = (float) (totalVideoCompleted * 100) / totalVideoByLesson;
        } else {
            progress = 0.0f;
        }

        Optional<Progress> checkProgress = progressRepository.findByCourseIdAndUserId(courseId, userId);
        if (checkProgress.isPresent()) {
            Progress existingProgress = checkProgress.get();
            existingProgress.setCompletionPercentage(progress);
            existingProgress.setStatus(progress == 100.0f ? "Completed" : "In Progress");
            existingProgress.setUpdatedDate(new Date());
            progressRepository.save(existingProgress);
            return existingProgress.getCompletionPercentage();
        }

        CourseResponse existingCourse = courseClient.getCourseByIdClient(courseId);
        if (existingCourse == null) {
            throw new DataNotFoundException("Course not found");
        }

        Progress newProcess = new Progress();
        try {
            newProcess.setUserId(userId);
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            newProcess.setCourseId(existingCourse.getId());
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }

        newProcess.setCompletionPercentage(progress);
        newProcess.setStatus(progress == 100.0f ? "Completed" : "In Progress");
        newProcess.setUpdatedDate(new Date());

        progressRepository.save(newProcess);
        return progress;
    }

    @Override
    public Optional<Progress> getProgressByAdmin(Long userId, Long courseId) {
        Optional<Progress> progressList = progressRepository.findByCourseIdAndUserId(courseId, userId);
        if (progressList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        return progressList;
    }

    @Override
    public Boolean checkProgressForCertificate(Long userId, Long courseId) {
        Optional<Progress> progress = progressRepository.findByCourseIdAndUserId(courseId, userId);
        if (progress.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }

        Progress progress1 = progressRepository.findProgressByCourseIdAndUserId(courseId, userId);
        if (progress1.getCompletionPercentage() != 100) {
            return false;
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course hasn't been completed");
        }
        return true;
    }

    @Override
    public Optional<Progress> getProgressByUser(Long courseId){
        Long userId = userClient.getUserIdByUsername();
        Long role = userClient.getRoleIdClient();
        if(role == 1) { //role = 3 -> teacher
            Optional<Enrollment> checkEnrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
            if (checkEnrollment.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "You must enroll this course first!!"
                );
            }
        } else  {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You dont have permission!!"
            );
        }
        Optional<Progress> progressList = progressRepository.findByCourseIdAndUserId(courseId, userId);
        if (progressList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        return progressList;
    }
}
