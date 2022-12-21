package com.sparta.blog.service;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    CommentRepository commentRepository;
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto){
        Comment comment = commentRepository.save(new Comment(commentRequestDto));
        return new CommentResponseDto(comment);
    }
}
