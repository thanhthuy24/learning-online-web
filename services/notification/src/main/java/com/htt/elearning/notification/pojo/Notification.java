package com.htt.elearning.notification.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "notification", schema = "elearningdb")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created_date")
    private Date createdDate;

    @Size(max = 4000)
    @Column(name = "title", length = 4000)
    private String title;

    @Size(max = 4000)
    @Column(name = "message", length = 4000)
    private String message;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_read")
    private Boolean isRead;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
    }

}