package com.htt.elearning.lesson.service;

import com.htt.elearning.lesson.dtos.LessonDTO;
import com.htt.elearning.lesson.dtos.LessonVideoDTO;
import com.htt.elearning.lesson.dtos.LessonVideoIntro;
import com.htt.elearning.lesson.exceptions.InvalidParamException;
import com.htt.elearning.lesson.pojo.Lesson;
import com.htt.elearning.video.dtos.VideoDTO;
import com.htt.elearning.video.pojo.Video;
import org.springframework.data.domain.*;

import java.util.List;

public interface LessonService {
    Lesson createLesson(LessonDTO lessonDTO);
    Lesson getLessonById(Long id);
    Page<Lesson> getAllLessons(String keyword, PageRequest pageRequest);
    Lesson updateLesson(Long id, LessonDTO lessonDTO);
    Lesson updateActiveLesson(Long id,LessonDTO lessonDTO);
    void deleteLesson(Long id);
    boolean existByName(String name);

    List<LessonVideoDTO> getLessonByCourseId(Long courseId);
    Video createVideo(Long lessonId, VideoDTO videoDTO) throws InvalidParamException;

    Long countLessonInCourse(Long courseId);

    LessonVideoIntro getFirstLesson(Long courseId);
}
