package com.htt.elearning.rating.service;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.rating.dto.CourseRatingDTO;
import com.htt.elearning.rating.pojo.Courserating;
import org.springframework.data.domain.*;

public interface CourseRatingService {
    Courserating createRating(CourseRatingDTO courseRatingDTO) throws DataNotFoundException;
    Page<Courserating> getRatingByCourseId(Long courseId, PageRequest pageRequest) throws DataNotFoundException;
    Float averageRatingByCourseId(Long courseId) throws DataNotFoundException;

    Long countAll(Long courseId) throws DataNotFoundException;
    Long countRatingByCourseIdByRating(Long courseId, Long rating) throws DataNotFoundException;
    Float averageRatingByStar(Long rate, Long courseId) throws DataNotFoundException;

    Page<Courserating> getRatingsBySentiment(Long courseId, String sentiment, PageRequest pageRequest);
    Page<Courserating> getRatingsByRate(Long courseId, Long rate, PageRequest pageRequest);
}
