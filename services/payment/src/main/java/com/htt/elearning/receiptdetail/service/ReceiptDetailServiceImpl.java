package com.htt.elearning.receiptdetail.service;

import com.htt.elearning.course.CourseClient;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.receipt.pojo.Receipt;
import com.htt.elearning.receipt.repository.ReceiptRepository;
import com.htt.elearning.receiptdetail.dto.ReceiptDetailDTO;
import com.htt.elearning.receiptdetail.pojo.Receiptdetail;
import com.htt.elearning.receiptdetail.repository.ReceiptDetailRepository;
import com.htt.elearning.user.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptDetailServiceImpl implements ReceiptDetailService {
    private final ReceiptDetailRepository receiptDetailRepository;
    private final ReceiptRepository receiptRepository;
    private final CourseClient courseClient;
    private final UserClient userClient;

    @Override
    public List<Receiptdetail> findByReceiptId(Long receiptId) {
        return receiptDetailRepository.findByReceiptId(receiptId);
    }

    @Override
    public Receiptdetail getReceiptDetail(Long id) {
        Receiptdetail receiptdetail = receiptDetailRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cannot find receipt detail id "  + id));
        return receiptdetail;
    }

    @Override
    public Receiptdetail createReceiptDetail(ReceiptDetailDTO receiptDetailDTO) throws DataNotFoundException {
        // tim xem receipt id co ton tai khong
        Receipt receipt = receiptRepository.findById(receiptDetailDTO.getReceiptId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cannot find receipt id " + receiptDetailDTO.getReceiptId()));
        CourseResponse courseResponse = courseClient.getCourseByIdClient(receiptDetailDTO.getCourseId());
        if (courseResponse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        Receiptdetail receiptDetail = Receiptdetail.builder()
                .receipt(receipt)
                .courseId(courseResponse.getId())
                .price(receiptDetailDTO.getPrice())
                .discount(receiptDetailDTO.getDiscount())
                .quantity(receiptDetailDTO.getQuantity())
                .build();
        return receiptDetailRepository.save(receiptDetail);
    }

    @Override
    public Page<Receiptdetail> getAllReceiptDetails(Pageable pageable, String keyword) {
        List<Long> courseIds = courseClient.getCoursesByKeywordClient(keyword);

        return receiptDetailRepository.findByCourseIdIn(courseIds, pageable);
    }
}
