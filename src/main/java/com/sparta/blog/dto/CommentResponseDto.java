package com.sparta.blog.dto;

import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long blogId;
    private String contents;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.blogId = comment.getBlog().getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
