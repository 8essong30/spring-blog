package com.sparta.blog.service;

import com.sparta.blog.dto.LoginRequestDto;
import com.sparta.blog.dto.SignupRequestDto;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseEntity<String> signup(SignupRequestDto signupRequestDto) {

        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        //회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자!");
        }

        // 사용자 role 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("틀린 관리자 암호!");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);

        return new ResponseEntity<>("회원가입 성공!", HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public String login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("미등록 사용자!")
        );

        // 비밀번호 확인
        if (!user.isValidPassword(loginRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치!");
        }

        return jwtUtil.createToken(user.getUsername(), user.getRole());
    }
}
