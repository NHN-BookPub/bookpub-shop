package com.nhnacademy.bookpubshop.coupontype.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.coupontype.dto.response.GetCouponTypeResponseDto;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import java.util.List;
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
    @DisplayName("쿠폰유형 save 테스트")
    void couponTypeSaveTest() {
        entityManager.persist(couponType);

        Optional<CouponType> result = couponTypeRepository.findById(couponType.getTypeNo());
        assertThat(result).isPresent();
        assertThat(result.get().getTypeNo()).isEqualTo(couponType.getTypeNo());
        assertThat(result.get().getTypeName()).isEqualTo(couponType.getTypeName());
    }

    @Test
    @DisplayName("쿠폰유형 단건 조회 성공 테스트")
    void getCouponTypeTest() {
        CouponType save = couponTypeRepository.save(couponType);

        Optional<GetCouponTypeResponseDto> result = couponTypeRepository.findByTypeNo(save.getTypeNo());

        assertThat(result).isPresent();
        assertThat(result.get().getTypeNo()).isEqualTo(save.getTypeNo());
        assertThat(result.get().getTypeName()).isEqualTo(save.getTypeName());
    }

    @Test
    @DisplayName("쿠폰유형 리스트 조회 성공 테스트")
    void getCouponTypesTest() {
        CouponType save = couponTypeRepository.save(couponType);

        List<GetCouponTypeResponseDto> result = couponTypeRepository.findAllBy();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTypeNo()).isEqualTo(save.getTypeNo());
        assertThat(result.get(0).getTypeName()).isEqualTo(save.getTypeName());
    }
}