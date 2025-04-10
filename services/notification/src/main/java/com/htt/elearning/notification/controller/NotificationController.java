package com.htt.elearning.notification.controller;

import com.htt.elearning.notification.dto.NotificationDTO;
import com.htt.elearning.notification.pojo.Notification;
import com.htt.elearning.notification.response.NotificationListResponse;
import com.htt.elearning.notification.response.NotificationResponse;
import com.htt.elearning.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/get-notification")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getNotification(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        Pageable pageRequest = PageRequest.of(page, limit,
                Sort.by("createdDate").descending());
        Page<Notification> notificationPage = notificationService.findNotificationsByUserId(pageRequest);

        // lay tong so trang
        int totalPage = notificationPage.getTotalPages();
        List<Notification> notifications = notificationPage.getContent();
        return ResponseEntity.ok(NotificationListResponse.builder()
                .notifications(notifications)
                .totalPages(totalPage)
                .build());
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public NotificationResponse createNotification(
//            @RequestBody NotificationDTO notificationDTO
//    ) {
//        NotificationResponse notification = notificationService.createNotification(notificationDTO);
//        return notification;
//    }

    @PatchMapping("/mark-read/{notificationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> markNotificationRead(
            @PathVariable Long notificationId
    ){
        return ResponseEntity.ok(notificationService.updateNotification(notificationId));
    }
}
