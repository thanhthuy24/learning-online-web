package com.htt.elearning.videocompleted.service;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.videocompleted.dto.VideoCompletedDTO;
import com.htt.elearning.videocompleted.pojo.Videocompleted;

import java.util.List;

public interface VideoCompletedService {
    Videocompleted createVideocompleted(VideoCompletedDTO videoCompleteDTO) throws DataNotFoundException;
    List<Videocompleted> getVideoCompletedBy(Long userId) throws DataNotFoundException;
    Long countVideoCompletedBy(Long lessonId) throws DataNotFoundException;

    List<Videocompleted> getVideoCompletedByLessonId(Long lessonId);
}
