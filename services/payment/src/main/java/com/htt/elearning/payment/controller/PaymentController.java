package com.htt.elearning.payment.controller;


import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.payment.components.MomoService;
import com.htt.elearning.receipt.pojo.Cart;
import com.htt.elearning.receipt.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/payment")
public class PaymentController {
    private final MomoService momoService;
    private final ReceiptService receiptService;

    @PostMapping("/create-payment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, Object>> createPayment(
            @RequestBody Map<String, String> params
    ) throws Exception {
        String orderId = params.get("orderId");
        String amount = params.get("amount");
        String returnUrl = params.get("returnUrl");

        if (orderId == null || amount == null || returnUrl == null) {
            throw new IllegalArgumentException("Missing required parameters");
        }

        float amountValue;
        try {
            amountValue = Float.parseFloat(amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format", e);
        }

        try {
            String payUrl = momoService.createPaymentRequest(orderId, amountValue, returnUrl);
            return ResponseEntity.ok(Map.of("payUrl", payUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Lỗi khi tạo thanh toán"));
        }
//        return this.momoService.createPaymentRequest(orderId, amountValue, returnUrl);
    }

    @PostMapping("/update-payment")
    @ResponseStatus(HttpStatus.CREATED)
    public void pay(@RequestBody List<Cart> carts) throws DataNotFoundException {
        this.receiptService.addReceipt(carts);
    }

    @GetMapping("/check-payment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> handleMomoCallback(@RequestParam Map<String, String> params) {
        boolean isSuccess = momoService.processMomoPayment(params);

        if (isSuccess) {
            return ResponseEntity.ok("Payment success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed");
        }
    }
}
