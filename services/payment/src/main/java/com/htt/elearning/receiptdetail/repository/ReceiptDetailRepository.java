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

    @Query("""
    SELECT d.courseId, SUM((d.price * d.quantity) * (100 - d.discount)/100) as total
    FROM Receiptdetail d
    GROUP BY d.courseId
    ORDER BY total DESC
    """)
    List<Object[]> findTop5Courses(Pageable pageable);

    @Query("""
    SELECT TO_CHAR(r.orderDate, 'YYYY-MM') AS month,
       SUM((d.price * d.quantity) * (100 - d.discount)/100) as total
    FROM Receiptdetail d
    JOIN d.receipt r
    GROUP BY TO_CHAR(r.orderDate, 'YYYY-MM')
    ORDER BY TO_CHAR(r.orderDate, 'YYYY-MM') ASC
    """)
    List<Object[]> getMonthlyRevenue();
}
