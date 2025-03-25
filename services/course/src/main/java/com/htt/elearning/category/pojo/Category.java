package com.htt.elearning.category.pojo;

import com.htt.elearning.course.pojo.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "category")
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Course> courses = new LinkedHashSet<>();

    public void setId(Long id) {  // Thêm thủ công setter
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {  // Thêm thủ công setter
        this.name = name;
    }

    public String getName() {
        return name;
    }

}