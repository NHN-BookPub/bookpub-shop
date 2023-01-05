package com.nhnacademy.bookpubshop.inquiry.repository;

import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품문의(inquiry) 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}