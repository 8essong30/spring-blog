package com.sparta.blog.dto;

import com.sparta.blog.entity.Comment;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String contents;

    public CommentRequestDto(Comment comment) {
        this.contents = comment.getContents();
    }
}
