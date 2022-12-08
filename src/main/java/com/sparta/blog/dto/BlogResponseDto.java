package com.sparta.blog.dto;

import com.sparta.blog.entity.Blog;
import lombok.Getter;

@Getter
public class BlogResponseDto {
    private String writer;
    private String title;
    private String contents;

    public BlogResponseDto(Blog blog) {
        this.writer = blog.getWriter();
        this.title = blog.getTitle();
        this.contents = blog.getContents();
    }

}
