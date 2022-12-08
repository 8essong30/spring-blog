package com.sparta.blog.entity;

import com.sparta.blog.dto.BlogRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Blog extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String password;

    public Blog(String writer, String title, String contents, String password) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.password = password;
    }

    public Blog(BlogRequestDto requestDto) {
        this.writer = requestDto.getWriter();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.password = requestDto.getPassword();
    }

    public void update(BlogRequestDto requestDto) {
        this.writer = requestDto.getWriter();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}
