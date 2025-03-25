package com.htt.elearning.video.service;

import com.htt.elearning.video.dtos.VideoDTO;
import com.htt.elearning.video.pojo.Video;

import java.util.List;

public interface VideoService {
    Long countVideoByLessonId(Long lessonId);
    List<Video> findByLessonId(Long lessonId);
    Video updateVideo(Long videoId, VideoDTO videoDTO);

    Long countVideoByCourseId(Long courseId);
}
