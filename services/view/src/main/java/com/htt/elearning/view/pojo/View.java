package com.htt.elearning.view.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "view")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "view_seq")
    @SequenceGenerator(name = "view_seq", sequenceName = "view_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "view_count")
    private Long viewCount;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
    }
}