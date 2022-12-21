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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final CommentService commentService;

    @PostMapping("blog/{blogId}/comment")
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
            Blog blog = blogRepository.findById(blogId).orElseThrow(
                    () -> new IllegalArgumentException("게시글 없음")
            );
            return commentService.createComment(commentRequest);
        }else {
            throw new IllegalArgumentException("없는 토큰");
        }
    }


}
