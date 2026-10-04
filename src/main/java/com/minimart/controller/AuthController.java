package com.minimart.controller;

import com.minimart.dto.LoginRequestDTO;
import com.minimart.dto.LoginResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO requestDTO) {

        if ("admin".equals(requestDTO.getUsername())
                && "admin123".equals(requestDTO.getPassword())) {

            return ResponseEntity.ok(
                    new LoginResponseDTO("Login successful"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO("Invalid credentials"));
    }
}
