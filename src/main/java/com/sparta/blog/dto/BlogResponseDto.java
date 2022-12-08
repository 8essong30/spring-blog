package com.sparta.blog.dto;

import com.sparta.blog.entity.Blog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BlogResponseDto {
    private String title;
    private String writer;
    private String contents;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;
    private String msg;

    public BlogResponseDto(Blog blog) {
        this.title = blog.getTitle();
        this.writer = blog.getWriter();
        this.contents = blog.getContents();
        this.createdAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();
    }

    public BlogResponseDto(String msg){
        this.msg = msg;
    }

}
