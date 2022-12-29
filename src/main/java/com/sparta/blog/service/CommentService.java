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

        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        // 그러면 이제 코멘트리스트에 새롭게 생성하면 돼
        Comment comment = commentRepository.save(new Comment(commentRequestDto, blog, requestedUsername));
        blog.addComment(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long blogId, Long commentId, CommentRequestDto requestDto, String requestedUsername){

        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        if (comment.isCommentWriter(requestedUsername)) { // 댓글의 작성자가 필요하네!!!!
            comment.updateComment(requestDto);
            return new CommentResponseDto(comment);
        } else {
            throw new IllegalArgumentException("작성자만 수정이 가능합니다.");
        }
    }

    @Transactional
    public CommentResponseDto updateCommentByAdmin(Long blogId, Long commentId, CommentRequestDto requestDto){

        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        comment.updateComment(requestDto);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long blogId, Long commentId, String requestedUsername) {

        Blog bLog = blogRepository.findById(blogId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        if (comment.isCommentWriter(requestedUsername)) { // 댓글의 작성자가 필요하네!!!!
            commentRepository.delete(comment);
        } else {
            throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
        }
    }

    @Transactional
    public void deleteCommentByAdmin(Long blogId, Long commentId) {
        Blog bLog = blogRepository.findById(blogId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
        commentRepository.delete(comment);
    }
}
