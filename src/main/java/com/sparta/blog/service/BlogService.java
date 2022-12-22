package com.sparta.blog.service;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BlogRepository;

import com.sparta.blog.repository.UserRepository;
import io.jsonwebtoken.Claims;
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
    private final UserRepository userRepository;

    @Transactional
    public BlogResponseDto createBlog(BlogRequestDto blogRequestDto, String requestedUsername) {

        User user = userRepository.findByUsername(requestedUsername).orElseThrow(
                () -> new IllegalArgumentException("사용자 없어!")
        );

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
    public BlogResponseDto updateBlog(Long blogId, BlogRequestDto requestDto, String requestedUsername) {

        User user = userRepository.findByUsername(requestedUsername).orElseThrow(
                () -> new IllegalArgumentException("사용자 없어!")
        );
        Blog blog = blogRepository.findByIdAndUserId(blogId, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("게시글 없어!")
        );

        blog.update(requestDto);
        return new BlogResponseDto(blog);
    }


    public ResponseEntity<String> deleteBlog(Long id, String requestedUsername) {

        User user = userRepository.findByUsername(requestedUsername).orElseThrow(
                () -> new IllegalArgumentException("사용자 없어!")
        );

        Blog blog = blogRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("게시글 없어!")
        );

        blogRepository.deleteById(id);
        return new ResponseEntity<>("삭제 성공!", HttpStatus.OK);
    }
}
