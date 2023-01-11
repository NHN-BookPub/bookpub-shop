package com.nhnacademy.bookpubshop.inquirycode.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.inquirycode.dummy.InquiryCodeDummy;
import com.nhnacademy.bookpubshop.inquirycode.entity.InquiryCode;
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
class InquiryCodeRepositoryTest {
    @Autowired
    InquiryCodeRepository inquiryCodeRepository;

    @Autowired
    TestEntityManager entityManager;

    InquiryCode inquiryCode;

    @BeforeEach
    void setUp() {
        inquiryCode = InquiryCodeDummy.dummy();
    }

    @Test
    @DisplayName("문의코드테이블 저장 테스트")
    void inquiryCodeTest() {
        InquiryCode persist = entityManager.persist(inquiryCode);
        Optional<InquiryCode> findInquiryCode = inquiryCodeRepository.findById(persist.getInquiryCodeNo());

        assertThat(findInquiryCode).isPresent();
        assertThat(findInquiryCode.get().getInquiryCodeInfo()).isEqualTo(persist.getInquiryCodeInfo());
        assertThat(findInquiryCode.get().getInquiryCodeName()).isEqualTo(persist.getInquiryCodeName());
        assertThat(findInquiryCode.get().isInquiryCodeUsed()).isTrue();
    }
}