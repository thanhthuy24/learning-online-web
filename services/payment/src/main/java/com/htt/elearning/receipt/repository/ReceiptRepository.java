package com.htt.elearning.receipt.repository;

import com.htt.elearning.receipt.pojo.Receipt;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByUserId(Long userId);
    @Query("SELECT r FROM Receipt r WHERE LOWER(r.totalMoney) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Receipt> searchReceiptsAll(@Param("keyword") String keyword, Pageable pageable);

}
