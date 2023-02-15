package com.nhnacademy.bookpubshop.personalinquiry.repository;

import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 1대1상담테이블을 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface PersonalInquiryRepository extends JpaRepository<PersonalInquiry, Long>,
        PersonalInquiryRepositoryCustom {
}
