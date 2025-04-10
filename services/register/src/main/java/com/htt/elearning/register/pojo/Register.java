package com.htt.elearning.register.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "register")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "register_seq")
    @SequenceGenerator(name = "register_seq", sequenceName = "register_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 4000)
    @NotNull
    @Column(name = "reason", nullable = false, length = 4000)
    private String reason;

    @Size(max = 4000)
    @NotNull
    @Column(name = "position", nullable = false, length = 4000)
    private String position;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "user_id")
    private Long userId;

}