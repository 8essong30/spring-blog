package com.sparta.blog.controller;

import com.sparta.blog.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/blog")
public class UserController {

    @PostMapping("/signup")
    public String signup(SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

}
