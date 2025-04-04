package com.htt.elearning.receipt.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.htt.elearning.receipt.pojo.Receipt;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptResponse {
    @JsonProperty("order_date")
    private Date orderDate;
    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be >= 0")
    private Float totalMoney;
    @JsonProperty("user_id")
    private Long userId;

    public static ReceiptResponse fromReceipt(Receipt receipt) {
        ReceiptResponse receiptResponse = ReceiptResponse.builder()
                .totalMoney(receipt.getTotalMoney())
                .userId(receipt.getUserId())
                .build();
        receiptResponse.setOrderDate(receipt.getOrderDate());
        return receiptResponse;
    }
}
