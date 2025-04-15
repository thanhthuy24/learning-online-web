package com.htt.elearning.rating.service;

import com.htt.elearning.comment.response.CommentResponse;
import com.htt.elearning.course.CourseClient;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.enrollment.pojo.Enrollment;
import com.htt.elearning.enrollment.repository.EnrollmentRepository;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.progress.pojo.Progress;
import com.htt.elearning.progress.repository.ProgressRepository;
import com.htt.elearning.rating.dto.CourseRatingDTO;
import com.htt.elearning.rating.pojo.Courserating;
import com.htt.elearning.rating.repository.CourseRatingRepository;
import com.htt.elearning.rating.response.RatingResponse;
import com.htt.elearning.sentiment.SentimentService;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseRatingServiceImpl implements CourseRatingService {
    private final EnrollmentRepository enrollmentRepository;
    private final ProgressRepository progressRepository;
    private final CourseRatingRepository courseRatingRepository;
    private final CourseClient courseClient;
    private final UserClient userClient;
    private final SentimentService sentimentService;
    private final HttpServletRequest request;

    @Override
    public Courserating createRating(CourseRatingDTO courseRatingDTO) throws DataNotFoundException {
        String token = request.getHeader("Authorization");
        CourseResponse existingCourse = courseClient.getCourseByIdClient(courseRatingDTO.getCourseId(), token);
        if (existingCourse == null) {
            new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Course not found!!");
        };

        Long userId = userClient.getUserIdByUsername(token);

        Optional<Enrollment> checkEnrollment = enrollmentRepository.
                findByUserIdAndCourseId(userId, courseRatingDTO.getCourseId());
        if (checkEnrollment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You must register course before rating!!!");
        }

        Optional<Progress> checkProgress = progressRepository
                .findByCourseIdAndUserId(existingCourse.getId(), userId);
        if (checkProgress.get().getStatus() == "In Progress") {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You must complete!!!");
        }

        Optional<Courserating> existingRating = courseRatingRepository
                .findByCourseIdAndUserId(courseRatingDTO.getCourseId(), userId);

        if (existingRating.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rating already exist!!!");
        }

        String sentiment = sentimentService.analyzeSentimentRating(courseRatingDTO.getComment());

        Courserating newRating = Courserating.builder()
                .rating(courseRatingDTO.getRating())
                .courseId(existingCourse.getId())
                .ratingDate(new Date())
                .comment(courseRatingDTO.getComment())
                .userId(userId)
                .sentiment(sentiment)
                .build();

        courseRatingRepository.save(newRating);
        return newRating;

    }

    @Override
    public Page<RatingResponse> getRatingByCourseId(Long courseId, PageRequest pageRequest) throws DataNotFoundException {
        String token = request.getHeader("Authorization");
        Page<Courserating> courseratingPage = courseRatingRepository.findByCourseId(courseId, pageRequest);
        List<Courserating> courseratings = courseRatingRepository.findByCourseId(courseId);

        TestCourseResponse course = courseClient.getFullCourseResponse(courseId);

        List<Long> userIds = courseratings.stream()
                .map(Courserating::getUserId)
                .distinct()
                .collect(Collectors.toList());

        List<UserResponse> users = userClient.getUsersByIdsClient(userIds, token);
        Map<Long, UserResponse> usersMap = users.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));

        List<RatingResponse> ratingResponses = courseratings.stream()
                .map(courserating -> {
                    UserResponse user = usersMap.get(courserating.getUserId());
                    return RatingResponse.fromRating(courserating, user, course);
                })
                .collect(Collectors.toList());
        return new PageImpl<>(ratingResponses, pageRequest, courseratingPage.getTotalElements());

//        CourseResponse existingCourse = courseClient.getCourseByIdClient(courseId, token);
//        if (existingCourse == null) {
//            new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, "Course not found!!");
//        };
//
//        return courseRatingRepository.findByCourseId(courseId, pageRequest)
//                .map(Courserating::fromRating);
    }

    @Override
    public Float averageRatingByCourseId(Long courseId) throws DataNotFoundException {
        String token = request.getHeader("Authorization");
        CourseResponse existingCourse = courseClient.getCourseByIdClient(courseId, token);
        if (existingCourse == null) {
            new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Course not found!!");
        };

        List<Courserating> getCourseRating = courseRatingRepository.findByCourseId(existingCourse.getId());
        Long countRating = courseRatingRepository.countByCourseId(existingCourse.getId());

        if (countRating == 0) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Don't have any rating to count!!!");
        }

        Long sum = 0L;
        for (Courserating courseRating : getCourseRating) {
            sum += courseRating.getRating();
        }

        // Calculate average and round to 1 decimal place
        Float averageRating = (float) sum / countRating;
        averageRating = Math.round(averageRating * 10) / 10.0f;

        return averageRating;
    }

    @Override
    public Long countAll(Long courseId) throws DataNotFoundException {
        String token = request.getHeader("Authorization");
        CourseResponse existingCourse = courseClient.getCourseByIdClient(courseId, token);
        if (existingCourse == null) {
            new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Course not found!!");
        };
        Long countRating = courseRatingRepository.countByCourseId(existingCourse.getId());

        if (countRating == 0) {
            return 0L;
        }
        return countRating;
    }

//    private Course getExistingCourse(Long courseId) throws DataNotFoundException {
//        return courseRepository.findById(courseId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "Course not found!!"));
//    }

    @Override
    public Long countRatingByCourseIdByRating(Long courseId, Long rating) throws DataNotFoundException {
        String token = request.getHeader("Authorization");
        CourseResponse existingCourse = courseClient.getCourseByIdClient(courseId, token);
        if (existingCourse == null) {
            new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Course not found!!");
        };

        Long countRating = courseRatingRepository.countByCourseIdAndRating(existingCourse.getId(), rating);
        if (countRating == 0) {
            return 0L;
        }
        return countRating;
    }

    @Override
    public Float averageRatingByStar(Long rate, Long courseId) throws DataNotFoundException {
        String token = request.getHeader("Authorization");
        CourseResponse existingCourse = courseClient.getCourseByIdClient(courseId, token);
        if (existingCourse == null) {
            new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Course not found!!");
        };

        List<Courserating> getCourseRating = courseRatingRepository.findByCourseId(existingCourse.getId());
        //đếm số luong tai khoan danh gia khoa hoc nay
        Long countAll = courseRatingRepository.countByCourseId(existingCourse.getId());
        Long countRate = courseRatingRepository.countByCourseIdAndRating(courseId, rate);

        if (countAll == 0) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Don't have any rating to count!!!");
        }

        Float averageRating = (float) countRate / countAll;

        // Nhân với 100 để chuyển thành phần trăm và làm tròn
        Float percentageRating = (float) Math.round(averageRating * 100);

        return percentageRating;
    }

    @Override
    public Page<RatingResponse> getRatingsBySentiment(Long courseId, String sentiment, PageRequest pageRequest) {
//        return courseRatingRepository.findByKeyword(courseId, sentiment, pageRequest);
        String token = request.getHeader("Authorization");
        Page<Courserating> courseratingPage = courseRatingRepository.findByKeyword(courseId, sentiment, pageRequest);
        List<Courserating> courseratings = courseratingPage.getContent();

        TestCourseResponse course = courseClient.getFullCourseResponse(courseId);

        List<Long> userIds = courseratings.stream()
                .map(Courserating::getUserId)
                .distinct()
                .collect(Collectors.toList());

        List<UserResponse> users = userClient.getUsersByIdsClient(userIds, token);
        Map<Long, UserResponse> usersMap = users.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));

        List<RatingResponse> ratingResponses = courseratings.stream()
                .map(courserating -> {
                    UserResponse user = usersMap.get(courserating.getUserId());
                    return RatingResponse.fromRating(courserating, user, course);
                })
                .collect(Collectors.toList());
        return new PageImpl<>(ratingResponses, pageRequest, courseratingPage.getTotalElements());
    }

    @Override
    public Page<RatingResponse> getRatingsByRate(Long courseId, Long rate, PageRequest pageRequest) {
        String token = request.getHeader("Authorization");
        Page<Courserating> courseratingPage = courseRatingRepository.findByRating(courseId, rate, pageRequest);
        List<Courserating> courseratings = courseratingPage.getContent();

        TestCourseResponse course = courseClient.getFullCourseResponse(courseId);

        List<Long> userIds = courseratings.stream()
                .map(Courserating::getUserId)
                .distinct()
                .collect(Collectors.toList());

        List<UserResponse> users = userClient.getUsersByIdsClient(userIds, token);
        Map<Long, UserResponse> usersMap = users.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));

        List<RatingResponse> ratingResponses = courseratings.stream()
                .map(courserating -> {
                    UserResponse user = usersMap.get(courserating.getUserId());
                    return RatingResponse.fromRating(courserating, user, course);
                })
                .collect(Collectors.toList());
        return new PageImpl<>(ratingResponses, pageRequest, courseratingPage.getTotalElements());
    }
}
