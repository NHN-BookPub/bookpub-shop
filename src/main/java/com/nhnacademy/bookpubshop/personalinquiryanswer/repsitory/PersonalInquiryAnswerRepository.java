package com.nhnacademy.bookpubshop.personalinquiryanswer.repsitory;

import com.nhnacademy.bookpubshop.personalinquiryanswer.entity.PersonalInquiryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 1:1문의 답변 테이블을 Database 에서 사용하기위한 Repo 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface PersonalInquiryAnswerRepository extends
        JpaRepository<PersonalInquiryAnswer, Long> {
}
