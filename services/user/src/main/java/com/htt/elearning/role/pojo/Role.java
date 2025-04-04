package com.htt.elearning.role.pojo;

import com.htt.elearning.user.pojo.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "role", schema = "elearningdb")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    public static String ADMIN = "ADMIN";
    public static String USER = "USER";
    public static String TEACHER = "TEACHER";

    @OneToMany(mappedBy = "role")
    private Set<User> users = new LinkedHashSet<>();

}
