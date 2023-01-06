package com.nhnacademy.bookpubshop.inquirycode.repository;

import com.nhnacademy.bookpubshop.inquirycode.entity.InquiryCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품문의코드 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface InquiryCodeRepository extends JpaRepository<InquiryCode, Integer> {

}
