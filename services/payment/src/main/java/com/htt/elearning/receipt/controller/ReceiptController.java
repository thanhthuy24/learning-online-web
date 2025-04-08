package com.htt.elearning.receipt.controller;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.receipt.dto.ReceiptDTO;
import com.htt.elearning.receipt.pojo.Cart;
import com.htt.elearning.receipt.pojo.Receipt;
import com.htt.elearning.receipt.response.ReceiptListResponse;
import com.htt.elearning.receipt.service.ReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptService receipService;

    @PostMapping("")
    public ResponseEntity<?> createReceipt(
            @Valid @RequestBody ReceiptDTO receiptDTO,
            BindingResult rs
    ) {
        try{
            if(rs.hasErrors()){
                List<String> errorMessages = rs.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            receipService.createReceipt(receiptDTO);
            return ResponseEntity.ok(receiptDTO);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReceipts(
            @PathVariable("userId") Long userId
    ) {
        try{
            List<Receipt> receipts = receipService.findByUserId(userId);
            return ResponseEntity.ok(receipts);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReceipt(
            @PathVariable("id") Long id
    ) {
        try{
            Receipt existingReceipt = receipService.getReceipt(id);
            return ResponseEntity.ok(existingReceipt);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReceipt(
            @Valid @PathVariable Long id,
            @Valid @RequestBody ReceiptDTO receiptDTO
    ) {
        try{

            return ResponseEntity.ok("update receipt của id = " + id);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReceipt(
            @Valid @PathVariable Long id
    ) {
        try{
            return ResponseEntity.ok("xóa receipt của id = " + id);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create-payment")
    @ResponseStatus(HttpStatus.CREATED)
    public void addReceipt(
            @RequestBody List<Cart> carts
    ) throws DataNotFoundException {
        this.receipService.addReceipt(carts);
    }

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReceiptListResponse> getAllReceiptsByAdmin(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "keyMoney", required = false, defaultValue = "") Float keyMoney
    ){
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by(direction, sortBy));
        Page<Receipt> receiptPage = receipService.getAllReceipts(pageRequest, keyword);

        int totalPage = receiptPage.getTotalPages();
        List<Receipt> receipts = receiptPage.getContent();
        return ResponseEntity.ok(ReceiptListResponse.builder()
                .receipts(receipts)
                .totalPages(totalPage)
                .build());
    }
}
