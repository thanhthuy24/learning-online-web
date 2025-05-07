package com.htt.elearning.interesting.pojo;

import com.htt.elearning.user.pojo.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "userinteresting", schema = "elearningdb")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Userinteresting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "category_id")
    private Long categoryId;

}