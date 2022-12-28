package com.sparta.blog.dto.response;

import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BlogResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    private List<CommentResponseDto> comments; // 여기서 본체가 나가면 안된다굿 entity가 밖으로 나가면 안돼

    public BlogResponseDto(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.username = blog.getUser().getUsername();
        this.contents = blog.getContents();
        this.createdAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();

        List<CommentResponseDto> list = new ArrayList<>();
        for (Comment comment : blog.getComments()) {
            list.add(new CommentResponseDto(comment));
        }
        this.comments = list;

/*
        this.comments = blog.getComments();
        // getComments의 댓글리스트에서 Comment 클래스에 blog에 Getter가 붙어있어
        // jackcon형님이 데이터를 json형식으로 변환해주려고 하는데 getter가 있어서 comment -> blog -> comment -> blog 반복
        // dto 만들면 해결돼 안만들어서 그런거임
*/
    }
}
