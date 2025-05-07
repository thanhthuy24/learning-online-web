package com.htt.elearning.overview.pojo;

import com.htt.elearning.user.pojo.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "useroverview", schema = "elearningdb")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Useroverview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "gender")
    private String gender;

    @Size(max = 255)
    @Column(name = "job")
    private String job;

    @Column(name = "daily_hours")
    private Float dailyHours;

    @Column(name = "tag")
    private Long tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}