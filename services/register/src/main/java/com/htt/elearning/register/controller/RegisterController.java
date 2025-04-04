package com.htt.elearning.register.controller;

import com.htt.elearning.register.dto.RegisterDTO;
import com.htt.elearning.register.pojo.Register;
import com.htt.elearning.register.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/register")
public class RegisterController {
    private final RegisterService registerService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllRegister() {
        List<Register> list = registerService.getAllRegisters();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{registerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Register> getRegisterById(
            @PathVariable Long registerId
    ) {
        Register registerById = registerService.getRegisterById(registerId);
        return ResponseEntity.ok(registerById);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createRegister(
            @Valid
            @RequestBody RegisterDTO registerDTO,
            BindingResult rs
    ){
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        registerService.createRegister(registerDTO);
        return ResponseEntity.ok(registerDTO);
    }


    @PatchMapping("/update/{registerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateRegister(
            @PathVariable Long registerId,
            @Valid @RequestBody RegisterDTO registerDTO,
            BindingResult rs
    ) {
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }

        registerService.updateRegister(registerDTO, registerId);
        return ResponseEntity.ok(registerDTO);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getRegisterByUserId(
            @PathVariable Long userId
    ){
        Register r = registerService.getRegisterByUserId(userId);
        if (r == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(registerService.getRegisterByUserId(userId));
    }

    @GetMapping("/user-form/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getRegisterByUser(
            @PathVariable Long userId
    ){
//        Register r = registerService.getRegisterByUser(userId);
        return ResponseEntity.ok(registerService.getRegisterByUser(userId));
    }

    @GetMapping("/list-form/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getListFormByUser(
    ){
        return ResponseEntity.ok(registerService.getListFormByUserId());
    }
}
