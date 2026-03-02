package com.psy.orderPay.backend.service;

// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.psy.orderPay.backend.dto.LoginRequestDto;
import com.psy.orderPay.backend.dto.LoginResponseDto;
import com.psy.orderPay.backend.dto.SignRequestDto;
import com.psy.orderPay.backend.entity.User;
import com.psy.orderPay.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder;
    // private final JwtTokenProvider jwtTokenProvider;  ← 주석 처리

    public LoginResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        //     throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        // }

        // String token = jwtTokenProvider.createToken(user.getEmail());  ← 주석 처리
        return new LoginResponseDto("temp_token", user.getEmail(), user.getUserName());
    }

    public String signup(SignRequestDto requestDto) {
        User user = User.builder()
                        .userName(requestDto.getName())
                        .email(requestDto.getEmail())
                        .password(requestDto.getPassword())
                        .role("User")
                        .status("Use")
                        .build();
        userRepository.save(user);
        return "";
    }
}