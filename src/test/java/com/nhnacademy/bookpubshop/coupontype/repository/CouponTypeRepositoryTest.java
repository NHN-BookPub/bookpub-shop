package com.nhnacademy.bookpubshop.coupontype.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 쿠폰유형 Repo Test.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@DataJpaTest
class CouponTypeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CouponTypeRepository couponTypeRepository;

    CouponType couponType;

    @BeforeEach
    void setUp() {
        couponType = CouponTypeDummy.dummy();
    }

    @Test
    @DisplayName(value = "쿠폰유형 save 테스트")
    void couponTypeSaveTest() {
        entityManager.persist(couponType);
        entityManager.clear();

        Optional<CouponType> result = couponTypeRepository.findById(couponType.getTypeNo());
        assertThat(result).isPresent();
        assertThat(result.get().getTypeNo()).isEqualTo(couponType.getTypeNo());
        assertThat(result.get().getTypeName()).isEqualTo(couponType.getTypeName());
    }
}