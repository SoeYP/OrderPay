package com.psy.orderPay.backend.controller;

import com.psy.orderPay.backend.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.psy.orderPay.backend.dto.LoginRequestDto;
import com.psy.orderPay.backend.dto.LoginResponseDto;
import com.psy.orderPay.backend.dto.SignRequestDto;

import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignRequestDto param) {
        String result = authService.signup(param);
        return ResponseEntity.ok(result);
    }
    
}
