package com.htt.elearning.interactions.pojo;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "interactions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interactions_seq")
    @SequenceGenerator(name = "interactions_seq", sequenceName = "interactions_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "clicks")
    private Long clicks;

    @Column(name = "purchased")
    private Boolean purchased;

}