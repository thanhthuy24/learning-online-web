package com.htt.elearning.receiptdetail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDetailDTO {
    @JsonProperty("receipt_id")
    @Min(value=1, message = "Receipt's ID must be > 0")
    private Long receiptId;

    @Min(value=1, message = "Course's ID must be > 0")
    @JsonProperty("course_id")
    private Long courseId;

    private Float price;
    @Min(value=1, message = "quantity must be >= 1")
    @JsonProperty("quantity")
    private Long quantity;
    @JsonProperty("discount")
    private Float discount;
}
