package com.sparta.blog.entity;

import com.sparta.blog.dto.CommentRequestDto;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "BLOG_ID")
    private Blog blog;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "USERS_ID")
    private User user;

    public Comment(CommentRequestDto commentRequestDto) {
        this.contents = commentRequestDto.getContents();
    }

    public void updateComment(String contents) {
        this.contents = contents;
    }
}
