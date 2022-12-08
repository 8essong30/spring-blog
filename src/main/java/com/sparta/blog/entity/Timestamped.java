package com.sparta.blog.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass //이 클래스를 상속하면 생성시간, 수정시간도 컬럼으로 인식하게 됨
@EntityListeners(AuditingEntityListener.class) //엔티티를 db에 적용하기 전,후에 커스텀 콜백을 요청, 요청할 클래스 인자로 지정
public class Timestamped {

    @CreatedDate //생성시간 저장
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정시간 저장
    private LocalDateTime modifiedAt;
}