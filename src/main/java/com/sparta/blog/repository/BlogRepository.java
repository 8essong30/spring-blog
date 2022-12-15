package com.sparta.blog.repository;

import com.sparta.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findAllByOrderByModifiedAtDesc(); //내림차순으로 정렬하라고 변수명 적은거임

    Optional<Blog> findByIdAndUserId(Long id, Long userId);
}
