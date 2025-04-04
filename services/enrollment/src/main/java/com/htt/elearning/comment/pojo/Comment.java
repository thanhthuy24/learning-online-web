package com.htt.elearning.comment.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "comment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 4000)
    @NotNull
    @Column(name = "content", nullable = false, length = 4000)
    private String content;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "user_id")
    private Long userId;

    @Size(max = 255)
    @Column(name = "sentiment")
    private String sentiment;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
    }

    public static Comment fromComment(Comment comment) {
        Comment cmt = Comment.builder()
                .id(comment.getId())
                .lessonId(comment.getLessonId())
                .content(comment.getContent())
                .userId(comment.getUserId())
                .build();
        cmt.setCreatedDate(comment.getCreatedDate());
        return cmt;
    }

}