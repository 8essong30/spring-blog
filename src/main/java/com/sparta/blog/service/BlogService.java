package com.sparta.blog.service;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.repository.BlogRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    @Transactional
    public BlogResponseDto createBlog(BlogRequestDto blogRequestDto) {
        Blog blog = new Blog(blogRequestDto);
        blogRepository.save(blog);
        return new BlogResponseDto(blog);
    }

    @Transactional(readOnly = true)
    public List<BlogResponseDto> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAllByOrderByModifiedAtDesc();
        List<BlogResponseDto> blogResponseDto = new ArrayList<>();
        for (Blog blog : blogs){
            blogResponseDto.add(new BlogResponseDto(blog)); //블로그의 항목을 dto로 묶어서 새로 생성해서 리스트 만들어
        }
        return blogResponseDto;
    }

    @Transactional(readOnly = true)
    public BlogResponseDto getBlogs(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new BlogResponseDto(blog);
    }

    @Transactional
    public BlogResponseDto updateBlog(Long id, BlogRequestDto requestDto) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        System.out.println(requestDto.getPassword());
        System.out.println(blog.getPassword());

        if (blog.getPassword().equals(requestDto.getPassword())) {
            blog.update(requestDto);
            return new BlogResponseDto(blog);
        }else {
            throw new IllegalStateException("비밀번호가 틀렸습니다!"); //기억 리턴타입이랑은 상관 없음..!
        }
    }

    public Boolean deleteBlog(Long id, String password) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        System.out.println("blog password = " + blog.getPassword());
        System.out.println("entered password = " + password);

        if(blog.getPassword().equals(password)) {
            blogRepository.deleteById(id);
            return true;
        }else{
            return false;
        }

    }
}
