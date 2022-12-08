package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/blog")
    public BlogResponseDto createBlog(@RequestBody BlogRequestDto requestDto) {
        return blogService.createBlog(requestDto);
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
    public BlogResponseDto updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
        return blogService.updateBlog(id, requestDto);
    }

    @DeleteMapping("/blog/{id}")
    public Boolean deleteBlog(@PathVariable Long id, @RequestParam String password) {
        return blogService.deleteBlog(id, password);
    }
}
