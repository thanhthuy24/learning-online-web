package com.htt.elearning.receipt.service;

import com.htt.elearning.course.CourseClient;
import com.htt.elearning.enrollment.EnrollmentClient;
import com.htt.elearning.enrollment.dto.EnrollmentDTO;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.receipt.dto.ReceiptDTO;
import com.htt.elearning.receipt.pojo.Cart;
import com.htt.elearning.receipt.pojo.Receipt;
import com.htt.elearning.receipt.repository.ReceiptRepository;
import com.htt.elearning.receipt.response.ReceiptResponse;
import com.htt.elearning.receiptdetail.pojo.Receiptdetail;
import com.htt.elearning.receiptdetail.repository.ReceiptDetailRepository;
import com.htt.elearning.user.UserClient;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {
    private final EnrollmentClient enrollmentClient;
    private final ReceiptRepository receiptRepository;
    private final ReceiptDetailRepository receiptDetailRepository;
    private final UserClient userClient;
    private final CourseClient courseClient;
    private final ModelMapper modelMapper;

    @Override
    public Page<Receipt> getAllReceipts(PageRequest pageRequest, String keyword) {
        return receiptRepository
                .searchReceiptsAll(keyword, pageRequest);
    }

    @Override
    public List<Receipt> findByUserId(Long userId) {
        return receiptRepository.findByUserId(userId);
    }

    @Override
    public Receipt getReceipt(Long id) {
        return receiptRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Can not find receipt with id " + id));

    }

    @Override
    public Receipt createReceipt(ReceiptDTO receiptDTO) {
        //convert ReceiptDTO -> Receipt in database
        // tao luong bang anh xa rieng de kiem soat viec anh xa
        modelMapper.typeMap(ReceiptDTO.class, Receipt.class)
                .addMappings(mapper -> mapper.skip(Receipt::setId));

        Long userId = userClient.getUserIdByUsername();

        //cap nhat cac truong cua don hang tu ReceiptDTO
        Receipt receipt = new Receipt();
        modelMapper.map(receiptDTO, receipt);
        receipt.setUserId(userId);
        receipt.setOrderDate(new Date());
        receiptRepository.save(receipt);
        return receipt;
    }

    @Override
    public Receipt updateReceipt(Long receiptId, ReceiptDTO receiptDTO) {
        return null;
    }

    @Override
    public void deleteReceipt(Long id) {

    }

    @Override
    public void addReceipt(List<Cart> cartList) throws DataNotFoundException {
        if (cartList != null){
            Long userId = userClient.getUserIdByUsername();

            for (Cart cart : cartList) {
                Long courseId = cart.getId();
                Long count = 0L;

                Boolean enrollment = enrollmentClient.checkEnrollment(userId, courseId);
                if (!enrollment) {
                    count++;
                }
                if (count > 0L) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This course had in your list course!!");
                }
            }

            Receipt receipt = new Receipt();
            receipt.setUserId(userId);
            receipt.setOrderDate(new Date());

            float totalPrice = (float) cartList.stream()
                    .mapToDouble(c ->(c.getPrice() * (1 - c.getDiscount() / 100)))
                    .sum();
            receipt.setTotalMoney(totalPrice);

            receiptRepository.save(receipt);

            for (Cart c : cartList) {
                Receiptdetail d = new Receiptdetail();
                d.setPrice(c.getPrice());
                d.setQuantity(1L);
                d.setDiscount(c.getDiscount());
                d.setReceipt(receipt);
                d.setCourseId(c.getId());

                receiptDetailRepository.save(d);

                EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
                enrollmentDTO.setEnrollmentDate(new Date());
                enrollmentDTO.setCourseId(c.getId());

                enrollmentClient.createEnrollmentClient(enrollmentDTO);

//                Enrollment enrollment = new Enrollment();
//                enrollment.setEnrollmentDate(new Date());
//                enrollment.setUser(this.userRepository.getUserByUsername(username));
//                enrollment.setCourse(courseRepository.getCourseById(c.getId()));
//
//                enrollmentRepository.save(enrollment);
            }

        }
    }}
