package com.htt.elearning.receiptdetail.service;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.receiptdetail.dto.ReceiptDetailDTO;
import com.htt.elearning.receiptdetail.pojo.Receiptdetail;
import org.springframework.data.domain.*;

import java.util.List;

public interface ReceiptDetailService {
    List<Receiptdetail> findByReceiptId(Long receiptId);
    Receiptdetail getReceiptDetail(Long id);
    Receiptdetail createReceiptDetail(ReceiptDetailDTO receiptDetailDTO) throws DataNotFoundException;
    Page<Receiptdetail> getAllReceiptDetails(Pageable pageable, String keyword);
}
