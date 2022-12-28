package com.sparta.blog.service;

import com.sparta.blog.dto.request.CommentRequestDto;
import com.sparta.blog.dto.response.CommentResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;

    @Transactional
    public CommentResponseDto createComment(Long blogId, CommentRequestDto commentRequestDto, String requestedUsername){
        User user = userRepository.findByUsername(requestedUsername).orElseThrow(
                () -> new IllegalArgumentException("사용자 없음")
        );

        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new IllegalArgumentException("게시글 없어!")
        );
        // 그러면 이제 코멘트리스트에 새롭게 생성하면 돼
        Comment comment = commentRepository.save(new Comment(commentRequestDto, blog, user.getUsername()));
        blog.addComment(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long blogId, Long commentId, CommentRequestDto requestDto, String requestedUsername){
        // 사용자, 게시글 확인
        User user = userRepository.findByUsername(requestedUsername).orElseThrow(
                () -> new IllegalArgumentException("없는 사용자임")
        );
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new IllegalArgumentException("없는 게시글")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("없는 댓글임")
        );

        if (comment.isCommentWriter(requestedUsername)) { // 댓글의 작성자가 필요하네!!!!
            comment.updateComment(requestDto);
            return new CommentResponseDto(comment);
        } else {
            throw new IllegalArgumentException("너가 쓴거 아니잖아 수정 못해");
        }
    }

    @Transactional
    public void deleteComment(Long blogId, Long commentId, String requestedUsername) {
        User user = userRepository.findByUsername(requestedUsername).orElseThrow(
                () -> new IllegalArgumentException("없는사용자여")
        );
        Blog bLog = blogRepository.findById(blogId).orElseThrow(
                () -> new IllegalArgumentException("없는 게시글이여")
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("없는 댓글이여")
        );

        if (comment.isCommentWriter(requestedUsername)) { // 댓글의 작성자가 필요하네!!!!
            commentRepository.delete(comment);
        } else {
            throw new IllegalArgumentException("너가 쓴거 아니잖아 수정 못해");
        }
    }



}
