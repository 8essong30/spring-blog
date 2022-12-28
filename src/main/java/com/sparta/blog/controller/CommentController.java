package com.sparta.blog.controller;

import com.sparta.blog.dto.request.CommentRequestDto;
import com.sparta.blog.dto.response.AuthenticatedUser;
import com.sparta.blog.dto.response.CommentResponseDto;
import com.sparta.blog.entity.UserRoleEnum;
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
    private final CommentService commentService;

    @PostMapping("/{blogId}/comment")
    public CommentResponseDto createComment(@PathVariable Long blogId, @RequestBody CommentRequestDto commentRequest, HttpServletRequest request) {
        //Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);

        // 토큰 유효성 검증, 토큰에서 사용자 정보 가져오기
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            return commentService.createComment(blogId, commentRequest, authenticatedUser.getUsername());
        } else {
            throw new IllegalArgumentException("없는 토큰");
        }
    }

    @PutMapping("/{blogId}/comment/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long blogId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            return commentService.updateComment(blogId, commentId, requestDto, authenticatedUser.getUsername());
        } else {
            throw new IllegalArgumentException("없는 토큰");
        }
    }

    @PutMapping("/admin/{blogId}/comment/{commentId}")
    public CommentResponseDto updateCommentByAdmin(@PathVariable Long blogId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            if(!authenticatedUser.getUserRoleEnum().equals(UserRoleEnum.ADMIN))
                throw new IllegalArgumentException("권한이 없습니다");
            return commentService.updateCommentByAdmin(blogId, commentId, requestDto);
        } else {
            throw new IllegalArgumentException("없는 토큰");
        }
    }

    @DeleteMapping("/{blogId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long blogId, @PathVariable Long commentId, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            commentService.deleteComment(blogId, commentId, authenticatedUser.getUsername());
            return new ResponseEntity<>("댓글 삭제 완료", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("댓글 삭제 실패", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/admin/{blogId}/comment/{commentId}")
    public ResponseEntity<String> deleteCommentByAdmin(@PathVariable Long blogId, @PathVariable Long commentId, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            if (!authenticatedUser.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) {
                throw new IllegalArgumentException("권한이 없습니다");
            }
            commentService.deleteCommentByAdmin(blogId, commentId);
            return new ResponseEntity<>("댓글 삭제 완료", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("댓글 삭제 실패", HttpStatus.BAD_REQUEST);
        }

    }


}
