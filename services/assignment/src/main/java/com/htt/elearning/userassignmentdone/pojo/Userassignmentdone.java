package com.htt.elearning.userassignmentdone.pojo;

import com.htt.elearning.assignment.pojo.Assignment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "userassignmentdone")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Userassignmentdone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userassignmentdone_seq")
    @SequenceGenerator(name = "userassignmentdone_seq", sequenceName = "userassignmentdone_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @Column(name = "user_id")
    private Long userId;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
    }

}