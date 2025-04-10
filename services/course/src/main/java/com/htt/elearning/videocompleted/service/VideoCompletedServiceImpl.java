package com.htt.elearning.videocompleted.service;

import com.htt.elearning.enrollment.EnrollmentClient;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.lesson.repository.LessonRepository;
import com.htt.elearning.progress.ProgressClient;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.video.pojo.Video;
import com.htt.elearning.video.repository.VideoRepository;
import com.htt.elearning.videocompleted.dto.VideoCompletedDTO;
import com.htt.elearning.videocompleted.pojo.Videocompleted;
import com.htt.elearning.videocompleted.repository.VideoCompletedRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoCompletedServiceImpl implements VideoCompletedService {
    private final VideoCompletedRepository videoCompletedRepository;
    private final VideoRepository videoRepository;
    private final UserClient userClient;
    private final EnrollmentClient enrollmentClient;
    private final ProgressClient progressClient;
    private final HttpServletRequest request;

    @Override
    public Videocompleted createVideocompleted(VideoCompletedDTO videoCompleteDTO) throws DataNotFoundException {
        Video existingVideo = videoRepository.findById(videoCompleteDTO.getVideoId())
                .orElseThrow(() -> new DataNotFoundException("Video not found!"));
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        Videocompleted checkVideoCompleted = videoCompletedRepository.findByVideoIdAndUserId(videoCompleteDTO.getVideoId(), userId);
        if (checkVideoCompleted != null) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Video already completed!");
        }

        Boolean checkEnrollment = enrollmentClient.checkEnrollment(userId, existingVideo.getLesson().getCourse().getId());
        if (checkEnrollment == null || !checkEnrollment) {
            new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This course isn't enrolled in your list! Please enroll before participating in this course!!");
        }

        Videocompleted videocompleted = Videocompleted.builder()
                .video(existingVideo)
                .userId(userId)
                .completedDate(new Date())
                .build();

        videoCompletedRepository.save(videocompleted);

        progressClient.createProgress(existingVideo.getLesson().getCourse().getId());

        return videocompleted;
    }

    @Override
    public List<Videocompleted> getVideoCompletedBy(Long userId) {
        String token = request.getHeader("Authorization");
        Long userID = userClient.getUserIdByUsernameClient(token);

        if (userID == userId){
            List<Videocompleted> listVideosCompleted = videoCompletedRepository.findByUserId(userID);
            if (listVideosCompleted.isEmpty()) {
                return null;
            }
            return listVideosCompleted;
        }
        return null;
    }

    @Override
    public Long countVideoCompletedBy(Long lessonId) throws DataNotFoundException {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);
        Long count = videoCompletedRepository.countWatchedVideosByUserAndLessonId(userId, lessonId);
        if (count == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found!");
        }
        return count;
    }
}
