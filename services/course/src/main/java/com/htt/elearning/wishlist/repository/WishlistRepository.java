package com.htt.elearning.wishlist.repository;

import com.htt.elearning.wishlist.pojo.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);

    Wishlist findByUserIdAndCourseId(Long userId, Long courseId);
}
