package com.htt.elearning.overview.repository;

import com.htt.elearning.overview.pojo.Useroverview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserOverviewRepository extends JpaRepository<Useroverview, Long> {
    @Query("""
        SELECT u 
        FROM Useroverview u
        WHERE u.user.id = :userId
    """)
    Useroverview findByUserId(Long userId);

}
