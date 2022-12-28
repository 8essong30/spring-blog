package com.sparta.blog.entity;

import com.sparta.blog.dto.request.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "BLOG_ID")
    private Blog blog;

    private String writer;

    public Comment(CommentRequestDto commentRequestDto, Blog blog, String writer) {
        this.contents = commentRequestDto.getContents();
        this.blog = blog;
        this.writer = writer;
    }

    public void updateComment(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

    public boolean isCommentWriter(String inputUsername) {
        return this.writer.equals(inputUsername);
    }
}
