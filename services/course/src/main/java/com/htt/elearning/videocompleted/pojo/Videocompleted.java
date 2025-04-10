package com.htt.elearning.videocompleted.pojo;

import com.htt.elearning.video.pojo.Video;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "videocompleted")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Videocompleted {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "videocompleted_seq")
    @SequenceGenerator(name = "videocompleted_seq", sequenceName = "videocompleted_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "completed_date")
    private Date completedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "video_id")
    private Video video;

    @Column(name = "user_id")
    private Long userId;

    @PrePersist
    protected void onCreate() {
        this.completedDate = new Date();
    }

}