package com.sparta.blog.service;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.BlogRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    @Transactional
    public BlogResponseDto createBlog(BlogRequestDto blogRequestDto, User user) {
        // 요청받은 Dto로 DB에 저장할 객체 만들기
       Blog blog = blogRepository.save(new Blog (blogRequestDto, user));
        return new BlogResponseDto(blog);
    }

    @Transactional(readOnly = true)
    public List<BlogResponseDto> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAllByOrderByModifiedAtDesc();
        List<BlogResponseDto> blogResponseDto = new ArrayList<>();
        for (Blog blog : blogs){
            blogResponseDto.add(new BlogResponseDto(blog));
        }
        return blogResponseDto;
    }

    @Transactional(readOnly = true)
    public BlogResponseDto getBlogs(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디 없어!")
        );
        return new BlogResponseDto(blog);
    }

    @Transactional
    public BlogResponseDto updateBlog(BlogRequestDto requestDto, Blog blog) {
        blog.update(requestDto);
        return new BlogResponseDto(blog);
    }


    public ResponseEntity<String> deleteBlog(Long id) {
        blogRepository.deleteById(id);
        return new ResponseEntity<>("삭제 성공!", HttpStatus.OK);
    }
}
