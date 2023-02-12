package com.nhnacademy.bookpubshop.inquirystatecode.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.inquirystatecode.dummy.InquiryCodeDummy;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 상품문의코드 레포지토리 테스트
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class InquiryStateCodeRepositoryTest {
    @Autowired
    InquiryStateCodeRepository inquiryStateCodeRepository;

    @Autowired
    TestEntityManager entityManager;

    InquiryStateCode inquiryStateCode;

    @BeforeEach
    void setUp() {
        inquiryStateCode = InquiryCodeDummy.dummy();
    }

    @Test
    @DisplayName("문의코드테이블 저장 테스트")
    void inquiryCodeTest() {
        InquiryStateCode persist = entityManager.persist(inquiryStateCode);
        Optional<InquiryStateCode> findInquiryCode = inquiryStateCodeRepository.findById(persist.getInquiryCodeNo());

        assertThat(findInquiryCode).isPresent();
        assertThat(findInquiryCode.get().getInquiryCodeInfo()).isEqualTo(persist.getInquiryCodeInfo());
        assertThat(findInquiryCode.get().getInquiryCodeName()).isEqualTo(persist.getInquiryCodeName());
        assertThat(findInquiryCode.get().isInquiryCodeUsed()).isTrue();
    }
}