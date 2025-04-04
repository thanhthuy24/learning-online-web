package com.htt.elearning.notification.controller;

import com.htt.elearning.configs.EmailService;
import com.htt.elearning.notification.dto.EmailRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> sendEmail(
            @RequestBody EmailRequestDTO emailRequestDTO
    ) {
        emailService.sendSimpleMail(emailRequestDTO.getTo(),
                emailRequestDTO.getUsername(), emailRequestDTO.getPassword());
        return ResponseEntity.ok(emailRequestDTO);
    }
}
