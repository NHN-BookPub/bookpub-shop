package com.nhnacademy.bookpubshop.member.event;

import com.nhnacademy.bookpubshop.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원가입 이벤트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class SignupEvent {
    private Member member;
}
