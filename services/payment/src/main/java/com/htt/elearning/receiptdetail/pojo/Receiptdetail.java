package com.htt.elearning.receiptdetail.pojo;

import com.htt.elearning.receipt.pojo.Receipt;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "receiptdetail")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receiptdetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receiptdetail_seq")
    @SequenceGenerator(name = "receiptdetail_seq", sequenceName = "receiptdetail_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "price")
    private Float price;

    @Column(name = "discount")
    private Float discount;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

}