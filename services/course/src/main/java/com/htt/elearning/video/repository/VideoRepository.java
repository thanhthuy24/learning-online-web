package com.htt.elearning.video.repository;

import com.htt.elearning.video.pojo.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByLessonId(Long lessonId);
    Optional<Video> findById(Long id);
    //    Long countByLessonId(List<Lesson> lessons);
    Long countByLessonId(Long lessonId);

    @Query("SELECT count(v) from Video v where v.lesson.course.id = :courseId")
    Long countByCourseId(@Param("courseId") Long courseId);
}
