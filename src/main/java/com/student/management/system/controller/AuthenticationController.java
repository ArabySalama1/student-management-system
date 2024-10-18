package com.student.management.system.controller;

import com.student.management.system.model.dto.LoginRequestDto;
import com.student.management.system.model.dto.LoginResponseDto;
import com.student.management.system.model.dto.RegisterRequestDto;
import com.student.management.system.model.dto.RegisterResponseDto;
import com.student.management.system.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {

        authenticationService.register(registerRequestDto);
        return ResponseEntity.ok(new RegisterResponseDto("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        String token = authenticationService.authenticate(loginRequestDto);
        ResponseCookie cookie =
                ResponseCookie.from("accessToken", token)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(Duration.ofMinutes(10))
                        .build();
        response.addHeader(SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new LoginResponseDto("User longed in successfully"));
    }


}
