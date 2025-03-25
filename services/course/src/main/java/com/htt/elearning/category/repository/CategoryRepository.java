package com.htt.elearning.category.repository;

import com.htt.elearning.category.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
