package com.nhnacademy.bookpubshop.inquirystatecode.repository;

import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품문의코드 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface InquiryStateCodeRepository extends JpaRepository<InquiryStateCode, Integer>,
        InquiryStateCodeRepositoryCustom {

}
