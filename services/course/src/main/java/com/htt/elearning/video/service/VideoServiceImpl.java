package com.htt.elearning.video.service;

import com.htt.elearning.lesson.pojo.Lesson;
import com.htt.elearning.lesson.repository.LessonRepository;
import com.htt.elearning.video.dtos.VideoDTO;
import com.htt.elearning.video.pojo.Video;
import com.htt.elearning.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final LessonRepository lessonRepository;

    @Override
    public Long countVideoByLessonId(Long lessonId) {
        Long number = videoRepository.countByLessonId(lessonId);
        if (number != null) {
            return number;
        }
        return 0L;
    }

    @Override
    public List<Video> findByLessonId(Long lessonId) {
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy bài học tương ứng"));
        List<Video> list = videoRepository.findByLessonId(lessonId);
        if (list != null) {
            return list;
        }
        return null;
    }

    @Override
    public Video updateVideo(Long videoId, VideoDTO videoDTO) {
        Video existingVideo = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find video with thid id"));
        if (existingVideo != null) {
            Lesson existingLesson = lessonRepository.findById(videoDTO.getLessonId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy bài học tương ứng"));
            existingVideo.setName(videoDTO.getName());
            existingVideo.setDescription(videoDTO.getDescription());
            existingVideo.setLesson((existingLesson));
            return videoRepository.save(existingVideo);
        }
        return null;
    }

    @Override
    public Long countVideoByCourseId(Long courseId) {
        return videoRepository.countByCourseId(courseId);
    }
}
