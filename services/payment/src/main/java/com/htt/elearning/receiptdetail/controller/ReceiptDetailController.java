package com.htt.elearning.receiptdetail.controller;

import com.htt.elearning.course.CourseClient;
import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.receiptdetail.dto.ReceiptDetailDTO;
import com.htt.elearning.receiptdetail.pojo.Receiptdetail;
import com.htt.elearning.receiptdetail.repository.ReceiptDetailRepository;
import com.htt.elearning.receiptdetail.response.ReceiptDetailListResponse;
import com.htt.elearning.receiptdetail.service.ReceiptDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/receipt_details")
@RequiredArgsConstructor
public class ReceiptDetailController {
    private final ReceiptDetailService receiptDetailService;
    private final ReceiptDetailRepository receiptDetailRepository;
    private final CourseClient courseClient;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReceiptDetailListResponse> getAllReceiptDetails(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
    ) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by(direction, sortBy));
        Page<Receiptdetail> receiptPage = receiptDetailService.getAllReceiptDetails(pageRequest, keyword);

        int totalPage = receiptPage.getTotalPages();
        List<Receiptdetail> receipts = receiptPage.getContent();
        return ResponseEntity.ok(ReceiptDetailListResponse.builder()
                .receiptDetails(receipts)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReceipt(
            @PathVariable("id") Long id
    ) {
        try{
            Receiptdetail existingReceiptdetail = receiptDetailService.getReceiptDetail(id);
            return ResponseEntity.ok(existingReceiptdetail);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createReceiptDetail(
            @Valid @RequestBody ReceiptDetailDTO receiptDetailDTO
    ) {
        try{
            Receiptdetail newReceiptDetail = receiptDetailService.createReceiptDetail(receiptDetailDTO);
            return ResponseEntity.ok().body(newReceiptDetail);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/receipt/{id}")
    public ResponseEntity<?> getReceiptDetails(
            @PathVariable("id") Long id
    ) {
        try{
            List<Receiptdetail> receiptdetails = receiptDetailService.findByReceiptId(id);
            return ResponseEntity.ok(receiptdetails);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/revenue/top-courses")
    public ResponseEntity<?> getTopCourses() {
        List<Object[]> result = receiptDetailRepository.findTop5Courses(PageRequest.of(0, 5));
        Map<Long, Double> courseRevenueMap = result.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Number) row[1]).doubleValue()
                ));

        List<Long> courseIds = new ArrayList<>(courseRevenueMap.keySet());

        List<TestCourseResponse> courses = courseClient.getFullCourseResponses(courseIds);

        // Gộp thông tin khóa học + doanh thu
        List<Map<String, Object>> finalResponse = courses.stream()
                .map(course -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("course", course);
                    item.put("totalRevenue", courseRevenueMap.get(course.getId()));
                    return item;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(finalResponse);
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<?> getMonthlyRevenue() {
        List<Object[]> data = receiptDetailRepository.getMonthlyRevenue();

        List<Map<String, Object>> result = data.stream().map(row -> {
            Map<String, Object> item = new HashMap<>();
            item.put("month", row[0]); // yyyy-MM format
            item.put("total", row[1]);
            return item;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
