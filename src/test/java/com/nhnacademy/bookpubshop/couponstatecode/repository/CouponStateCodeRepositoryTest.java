package com.nhnacademy.bookpubshop.couponstatecode.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 쿠폰상태코드 Repo Test.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@DataJpaTest
class CouponStateCodeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CouponStateCodeRepository couponStateCodeRepository;

    CouponStateCode couponStateCode;

    @BeforeEach
    void setUp() {
        couponStateCode = CouponStateCodeDummy.dummy();
    }

    @Test
    @DisplayName(value = "쿠폰상태코드 save 테스트")
    void CouponStateCodeSaveTest() {
        entityManager.persist(couponStateCode);
        entityManager.clear();

        Optional<CouponStateCode> result = couponStateCodeRepository.findById(couponStateCode.getCodeNo());

        assertThat(result).isPresent();
        assertThat(result.get().getCodeNo()).isEqualTo(couponStateCode.getCodeNo());
        assertThat(result.get().getCodeTarget()).isEqualTo(couponStateCode.getCodeTarget());
        assertThat(result.get().isCodeUsed()).isEqualTo(couponStateCode.isCodeUsed());
        assertThat(result.get().getCodeInfo()).isEqualTo(couponStateCode.getCodeInfo());
    }
}