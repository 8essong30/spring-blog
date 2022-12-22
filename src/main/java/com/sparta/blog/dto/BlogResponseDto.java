package com.sparta.blog.dto;

import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BlogResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    private List<Comment> comments;

    public BlogResponseDto(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.username = blog.getUser().getUsername();
        this.contents = blog.getContents();
        this.createdAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();
        this.comments = blog.getComments();
    }
}
