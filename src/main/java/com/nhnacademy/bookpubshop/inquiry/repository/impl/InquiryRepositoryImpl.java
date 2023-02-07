package com.nhnacademy.bookpubshop.inquiry.repository.impl;

import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.inquiry.repository.InquiryRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 상품문의 레포지토리 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class InquiryRepositoryImpl extends QuerydslRepositorySupport
        implements InquiryRepositoryCustom {
    public InquiryRepositoryImpl() {
        super(Inquiry.class);
    }

    //문의 관련 -> 모두 회원만 가능함,,? 1:1채팅 제외??
    // 상품문의(문의하기, 문의 내역 보기)
    // 불량상품 신고(신고하기, 신고 내역 보기)
    // 1:1 문의(문의하기, 문의 내역 보기)
    // 1:1 채팅(메인페이지에서 채팅하기, 채팅 내역 보기?)

    // 구매한 회원만 문의 가능, 비공개 및 공개 선택, tui editor 사용
    // 문의는 여러번 가능하며 depth는 한개만

    //관리자 -> 문의글 20개 페이징, 전체문의글 카운팅 및 답변완료 카운팅, 미완료된 문의글 부터 보이게,
    //문의 완료처리
    //문의 검색 가능 -> 상품 카테고리 + (상품명 + 상품코드 + 질문)

    // 회원 구현 ->
    // 문의 하기
    // 문의 내역 보기
    //

    // 관리자 구현 ->
}
