package com.htt.elearning.score.pojo;

import com.htt.elearning.assignment.pojo.Assignment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "score")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 4000)
    @Column(name = "feed_back", length = 4000)
    private String feedBack;

    @Column(name = "score")
    private Float score;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @Column(name = "user_id")
    private Long userId;

}