package com.htt.elearning.question.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.htt.elearning.assignment.pojo.Assignment;
import com.htt.elearning.choice.pojo.Choice;
import com.htt.elearning.essay.pojo.Essay;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "question")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_seq")
    @SequenceGenerator(name = "question_seq", sequenceName = "question_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 4000)
    @NotNull
    @Column(name = "content", nullable = false, length = 4000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Choice> choices = new LinkedHashSet<>();

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Essay> essays = new LinkedHashSet<>();

}