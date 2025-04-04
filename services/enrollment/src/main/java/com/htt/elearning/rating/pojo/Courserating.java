package com.htt.elearning.rating.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "courserating")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Courserating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 4000)
    @NotNull
    @Column(name = "comment", nullable = false, length = 4000)
    private String comment;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Long rating;

    @Column(name = "rating_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ratingDate;

    @Size(max = 255)
    @Column(name = "sentiment")
    private String sentiment;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "course_id")
    private Long courseId;

    @PrePersist
    protected void onCreate() {
        this.ratingDate = new Date();
    }

    public static Courserating fromRating(Courserating courserating) {
        Courserating courseRating = Courserating.builder()
                .id(courserating.getId())
                .rating(courserating.getRating())
                .courseId(courserating.getCourseId())
                .userId(courserating.getUserId())
                .comment(courserating.getComment())
                .build();
        courseRating.setRatingDate(courserating.getRatingDate());
        return courseRating;
    }

}