package com.htt.elearning.receiptdetail.response;

import com.htt.elearning.receiptdetail.pojo.Receiptdetail;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDetailListResponse {
    private List<Receiptdetail> receiptDetails;
    private int totalPages;
}