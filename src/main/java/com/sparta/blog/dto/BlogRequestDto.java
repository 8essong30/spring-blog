package com.sparta.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BlogRequestDto {
    private String writer;
    private String title;
    private String contents;
    private String password;
}
