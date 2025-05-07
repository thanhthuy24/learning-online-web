package com.htt.elearning.interesting.repository;

import com.htt.elearning.interesting.pojo.Userinteresting;
import com.htt.elearning.overview.pojo.Useroverview;
import com.htt.elearning.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserInterestingRepository extends JpaRepository<Userinteresting, Long> {
    List<Userinteresting> findByUserId(Long userId);

    Long user(User user);

    @Query("""
        SELECT c.categoryId FROM Userinteresting c
        WHERE c.user.id = :userId
        """)
    List<Long> getCategoryIdsByUserId(@Param("userId") Long userId);
}
