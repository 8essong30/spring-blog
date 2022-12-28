package com.sparta.blog.controller;

import com.sparta.blog.dto.request.BlogRequestDto;
import com.sparta.blog.dto.response.AuthenticatedUser;
import com.sparta.blog.dto.response.BlogResponseDto;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
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
    private final BlogService blogService;
    private final JwtUtil jwtUtil;

    @PostMapping("/blog")
    public BlogResponseDto createBlog(@RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        //Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        // 토큰 유효성 검증, 토큰에서 사용자 정보 가져오기
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            return blogService.createBlog(requestDto, authenticatedUser.getUsername());
        } else {
            throw new IllegalArgumentException("없는 토큰");
        }
    }

    @GetMapping("/blog")
    public List<BlogResponseDto> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/blog/{id}")
    public BlogResponseDto getBlog(@PathVariable Long id) {
        return blogService.getBlogs(id);
    }

    @PutMapping("/blog/{id}")
    public BlogResponseDto updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            return blogService.updateBlog(id, requestDto, authenticatedUser.getUsername());
        } else {
            throw new IllegalArgumentException("수정 실패");
        }
    }

    @PutMapping("/blog/admin/{id}")
    public BlogResponseDto updateBlogByAdmin(@PathVariable Long id, @RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            if (!authenticatedUser.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) throw new IllegalArgumentException("권한이 없습니다.");
            return blogService.updateBlogByAdmin(id, requestDto);
        } else {
            throw new IllegalArgumentException("수정 실패");
        }
    }

    @DeleteMapping("/blog/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long id, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);

        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            return blogService.deleteBlog(id, authenticatedUser.getUsername());
        } else {
            return new ResponseEntity<>("삭제 실패", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/blog/admin/{id}")
    public ResponseEntity<String> deleteBlogByAdmin(@PathVariable Long id, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);

        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            if (!authenticatedUser.getUserRoleEnum().equals(UserRoleEnum.ADMIN))
                throw new IllegalArgumentException("권한이 없습니다.");
            return blogService.deleteBlogByAdmin(id);
        } else {
            return new ResponseEntity<>("삭제 실패", HttpStatus.BAD_REQUEST);
        }
    }
}
