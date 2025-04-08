package com.htt.elearning.receiptdetail.repository;

import com.htt.elearning.receiptdetail.pojo.Receiptdetail;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceiptDetailRepository extends JpaRepository<Receiptdetail, Long> {
    List<Receiptdetail> findByReceiptId(Long receiptId);
//    @Query("SELECT r FROM Receiptdetail r WHERE LOWER(r.course.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
//            + "LOWER(r.receipt.user.username) LIKE LOWER(CONCAT('%', :keyword, '%')) ")

    Page<Receiptdetail> findByCourseIdIn(List<Long> courseIds, Pageable pageable);
}
