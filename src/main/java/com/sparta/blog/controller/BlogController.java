package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.User;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.repository.UserRepository;
import com.sparta.blog.service.BlogService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class BlogController {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final BlogService blogService;
    private final JwtUtil jwtUtil;

    @PostMapping("/blog")
    public BlogResponseDto createBlog(@RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        //Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        // 토큰 유효성 검증, 토큰에서 사용자 정보 가져오기
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                //토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰!!");
            }
            String requestedUserByToken = claims.getSubject();
            User user = userRepository.findByUsername(requestedUserByToken).orElseThrow(
                    () -> new IllegalArgumentException("사용자 없어!")
            );
            return blogService.createBlog(requestDto, user);
        } else {
            throw new IllegalArgumentException("없는 토큰");
        }
    }    

    @GetMapping("/blog")
    public List<BlogResponseDto> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/blog/{id}")
    public BlogResponseDto getBlog(@PathVariable Long id)
    {
        return blogService.getBlogs(id);
    }

    @PutMapping("/blog/{id}")
    public BlogResponseDto updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            }else {
                throw new IllegalArgumentException("유효하지 않은 토큰!!");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자 없어!")
            );
            Blog blog = blogRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("게시글 없어!")
            );
            return blogService.updateBlog(requestDto, blog);
        }else {
            throw new IllegalArgumentException("수정 실패");
        }
    }

    @DeleteMapping("/blog/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long id, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰!!");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자 없어!")
            );

            Blog blog = blogRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("게시글 없어!")
            );
            return blogService.deleteBlog(id);
        } else {
            return new ResponseEntity<>("삭제 실패", HttpStatus.BAD_REQUEST);
        }
    }
}
