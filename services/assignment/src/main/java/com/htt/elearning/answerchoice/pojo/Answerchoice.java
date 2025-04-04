package com.htt.elearning.answerchoice.pojo;

import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.choice.pojo.Choice;
import com.htt.elearning.question.pojo.Question;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "answerchoice")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Answerchoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "choice_id")
    private Choice choice;

    @Column(name = "user_id")
    private Long userId;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
    }

}