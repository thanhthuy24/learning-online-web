package com.htt.elearning.category.repository;

import com.htt.elearning.category.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
        SELECT c FROM Category c 
        WHERE c.id IN :ids
        """)
    List<Category> findAllByIds(@Param("ids") List<Long> ids);
}
