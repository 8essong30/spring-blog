package com.sparta.blog.service;

import com.sparta.blog.dto.LoginRequestDto;
import com.sparta.blog.dto.SignupRequestDto;
import com.sparta.blog.entity.User;
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

    @Transactional
    public ResponseEntity<String> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        //회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자!");
        }

        User user = new User(username, password);
        userRepository.saveAndFlush(user);

        return new ResponseEntity<>("회원가입 성공!", HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public String login(LoginRequestDto loginRequestDto) {  // 정말 login관련된 처리만 여기서 하도록 설계
        String username = loginRequestDto.getUsername();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("미등록 사용자!")
        );

        // 비밀번호 확인
        //1..비밀번호 확인 작업은 User가 해야할 의무가 있음. 역할을 객체로 옮겨
        if (!user.isValidPassword(loginRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치!");
        }

        // 3..httpServlet은 대체될 수 있다. 같은 기능을 제공하는 기술들이 많음
        // 이걸 바꾼다고 했을 때 httpServlet을 받는 애들은 다 변경이 일어나야함
        // 그래서 reponse를 안 남기는게 좋다..
        // 요청을 받거나 응답을 할 떄는 항상 스콥을 최소화하는게 좋음 그래야 결합도가 낮아질 수 있음
        // response가 필요한 이유를 찾아서 코드 변경

        String generatedToken = jwtUtil.createToken(user.getUsername());

        return generatedToken;
    }
}
