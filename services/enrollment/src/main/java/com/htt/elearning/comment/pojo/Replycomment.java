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
@Table(name = "replycomment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Replycomment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "replycomment_seq")
    @SequenceGenerator(name = "replycomment_seq", sequenceName = "replycomment_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 4000)
    @NotNull
    @Column(name = "content", nullable = false, length = 4000)
    private String content;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @PreUpdate
    protected void onUpdate() {
        this.createdDate = new Date();
    }

    public static Replycomment fromReplyComment(Replycomment replyComment) {
        Replycomment reply = Replycomment.builder()
                .id(replyComment.getId())
                .comment(replyComment.getComment())
                .content(replyComment.getContent())
                .userId(replyComment.getUserId())
                .build();
        reply.setCreatedDate(replyComment.getCreatedDate());
        return reply;
    }

}