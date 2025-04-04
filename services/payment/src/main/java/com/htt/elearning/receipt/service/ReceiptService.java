package com.htt.elearning.receipt.service;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.receipt.dto.ReceiptDTO;
import com.htt.elearning.receipt.pojo.Cart;
import com.htt.elearning.receipt.pojo.Receipt;
import org.springframework.data.domain.*;

import java.util.List;

public interface ReceiptService {
    Page<Receipt> getAllReceipts(PageRequest pageRequest, String keyword);

    List<Receipt> findByUserId(Long userId);
    Receipt getReceipt(Long id);
    Receipt createReceipt(ReceiptDTO receiptDTO);
    Receipt updateReceipt(Long receiptId, ReceiptDTO receiptDTO);
    void deleteReceipt(Long id);
    void addReceipt(List<Cart> cartList) throws DataNotFoundException;
}
