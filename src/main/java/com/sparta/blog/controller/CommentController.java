package com.sparta.blog.controller;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.User;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.repository.UserRepository;
import com.sparta.blog.service.CommentService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog")
public class CommentController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final CommentService commentService;

    @PostMapping("/{blogId}/comment")
    public CommentResponseDto createComment(@PathVariable Long blogId, @RequestBody CommentRequestDto commentRequest, HttpServletRequest request) {
        //Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);

        // 토큰 유효성 검증, 토큰에서 사용자 정보 가져오기
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                //토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰!!");
            }
            String requestedUsernameByToken = claims.getSubject();

            return commentService.createComment(blogId, commentRequest, requestedUsernameByToken);
        } else {
            throw new IllegalArgumentException("없는 토큰");
        }
    }

    @PutMapping("/{blogId}/comment/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long blogId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰!!");
            }
            String requestedUsernameByToken = claims.getSubject();

            return commentService.updateComment(blogId, commentId, requestDto, requestedUsernameByToken);
        } else {
            throw new IllegalArgumentException("없는 토큰");
        }
    }

    @DeleteMapping("/{blogId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long blogId, @PathVariable Long commentId, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰!!");
            }
            String requestedUsernameByToken = claims.getSubject();

            commentService.deleteComment(blogId, commentId, requestedUsernameByToken);
            return new ResponseEntity<>("댓글 삭제 완료", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("댓글 삭제 실패", HttpStatus.BAD_REQUEST);
        }

    }


}
