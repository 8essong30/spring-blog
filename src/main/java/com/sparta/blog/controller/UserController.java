package com.sparta.blog.controller;

import com.sparta.blog.dto.request.LoginRequestDto;
import com.sparta.blog.dto.request.SignupRequestDto;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String generatedToken = userService.login(loginRequestDto);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, generatedToken);
        return "로그인 성공!";
    }
}
