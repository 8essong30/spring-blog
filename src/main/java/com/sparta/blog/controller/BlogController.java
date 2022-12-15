package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Client라고 보통 표현. 앱, web, 또다른 서버가 될 수 있음
//서비스에 로직을 처리하라고 전달하고 받아서 클라이언트에 결과 전달

@RestController
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/blog")
    public BlogResponseDto createBlog(@RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        return blogService.createBlog(requestDto, request);
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
        return blogService.updateBlog(id, requestDto, request);
    }

    @DeleteMapping("/blog/{id}")
    public Long  deleteBlog(@PathVariable Long id, @RequestParam String password) {
        return blogService.deleteBlog(id, password);
    }
}
