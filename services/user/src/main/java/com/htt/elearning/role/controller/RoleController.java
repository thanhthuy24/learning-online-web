package com.htt.elearning.role.controller;

import com.htt.elearning.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRole());
    }
}
