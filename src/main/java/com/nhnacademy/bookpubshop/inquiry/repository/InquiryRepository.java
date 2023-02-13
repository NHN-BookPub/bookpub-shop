package com.nhnacademy.bookpubshop.inquiry.repository;

import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품문의(inquiry) 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface InquiryRepository extends JpaRepository<Inquiry, Long>, InquiryRepositoryCustom {
    /**
     * 해당 부모문의를 가지고 있는 모든 문의 리스트를 반환합니다.
     *
     * @param inquiryNo 부모상품문의번호
     * @return 해당 부모상품문의를 가지고 있는 문의 리스트
     */
    List<Inquiry> findByParentInquiry_InquiryNo(Long inquiryNo);

}