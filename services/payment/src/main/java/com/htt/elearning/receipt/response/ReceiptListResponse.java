package com.htt.elearning.receipt.response;

import com.htt.elearning.receipt.pojo.Receipt;
import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptListResponse {
    private List<Receipt> receipts;
    private int totalPages;
}
