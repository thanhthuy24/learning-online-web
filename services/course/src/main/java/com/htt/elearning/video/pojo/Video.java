package com.htt.elearning.video.pojo;

import com.htt.elearning.lesson.pojo.Lesson;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "video")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    public static final int MAXIMUM_VIDEOS_PER_LESSON = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 4000)
    @NotNull
    @Column(name = "description", nullable = false, length = 4000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

}