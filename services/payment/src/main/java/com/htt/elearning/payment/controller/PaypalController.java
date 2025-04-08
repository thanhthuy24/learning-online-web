package com.htt.elearning.payment.controller;

import com.htt.elearning.payment.components.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/paypal")
public class PaypalController {
    private final PaypalService paypalService;

    @PostMapping("/create-payment")
    public String createPayment(
            @RequestParam("amount") Double amount
    ) {
        String cancelURL =  "http://localhost:8082/paypal/cancel";
        String successURL =  "http://localhost:8082/paypal/success";

        try {
            Payment payment = paypalService.createPaymentByPaypal(
                    amount, "USD", "paypal", "sale", "Thanh toan don hang",
                    cancelURL, successURL
            );
            for (Links link : payment.getLinks()) {
                if(link.getRel().equals("approval_url")){
                    return link.getHref();
                }
            }

        } catch (PayPalRESTException e){
            e.printStackTrace();
        }
        return "/error";
    }

    @GetMapping("/success")
    public ResponseEntity<String> success(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String PayerID
    ) {
        try {
            // Thực hiện thanh toán
            paypalService.executePayment(paymentId, PayerID);
            // Trả về thông báo thành công
            return ResponseEntity.ok("Payment executed successfully");
        } catch (PayPalRESTException e) {
            // In ra lỗi nếu có
            e.printStackTrace();
            // Trả về thông báo lỗi nếu có vấn đề với PayPal
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error executing payment");
        }
    }


    @GetMapping("/check-payment")
    public ResponseEntity<Map<String, Object>> checkPayment(
            @RequestParam("paymentId") String paymentId
    ){
        Map<String, Object> response = new HashMap<>();
        try {
            // Lấy chi tiết giao dịch thanh toán từ PayPal
            Payment payment = paypalService.getPaymentDetail(paymentId);
            String paymentStatus = payment.getState();  // Trạng thái thanh toán

            // Đưa các thông tin vào response
            response.put("paymentStatus", paymentStatus);

            // Trả về ResponseEntity với mã trạng thái 200 OK và dữ liệu JSON
            return ResponseEntity.ok(response);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Không thể kiểm tra trạng thái thanh toán.");

            // Trả về lỗi 500 nếu gặp sự cố
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/cancel")
    public String paymentCancelled(@RequestParam("token") String token) {
        return "redirect:/";
    }

    @GetMapping("/error")
    public String paymentError() {
        return "paymentError";
    }
}
