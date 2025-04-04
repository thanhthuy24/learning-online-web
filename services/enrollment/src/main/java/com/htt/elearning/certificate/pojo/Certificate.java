package com.htt.elearning.certificate.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "certificate")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 4000)
    @NotNull
    @Column(name = "name", nullable = false, length = 4000)
    private String name;

    @NotNull
    @Column(name = "issuance_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date issuanceDate;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "user_id")
    private Long userId;

}