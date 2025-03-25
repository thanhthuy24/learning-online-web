package com.htt.elearning.course.pojo;

import com.htt.elearning.tag.pojo.Tag;
import com.htt.elearning.category.pojo.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "course")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 4000)
    @NotNull
    @Column(name = "description", nullable = false, length = 4000)
    private String description;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "price")
    private Float price;

    @Column(name = "discount")
    private Float discount;

    @Size(max = 255)
    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Column(name = "teacher_id")
    private Long teacherId;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
        if (this.isActive == null) {
            this.isActive = true; // Mặc định là true (hoặc 1)
        }
    }


}