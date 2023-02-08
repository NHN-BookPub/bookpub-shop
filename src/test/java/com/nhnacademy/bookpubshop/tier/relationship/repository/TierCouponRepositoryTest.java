package com.nhnacademy.bookpubshop.tier.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dummy.CouponTemplateDummy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.relationship.dto.response.GetTierCouponResponseDto;
import com.nhnacademy.bookpubshop.tier.relationship.dummy.TierCouponDummy;
import com.nhnacademy.bookpubshop.tier.relationship.entity.TierCoupon;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 등급 쿠폰 Repo test.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@DataJpaTest
class TierCouponRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TierCouponRepository tierCouponRepository;

    TierCoupon tierCoupon;

    BookPubTier tier;
    CouponTemplate couponTemplate;

    CouponPolicy couponPolicy;

    CouponType couponType;
    Product product;
    Category category;
    CouponStateCode couponStateCode;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicyDummy.dummy();
        couponType = CouponTypeDummy.dummy();
        category = CategoryDummy.dummy();
        couponStateCode = CouponStateCodeDummy.dummy();

        tier = TierDummy.dummy();
        couponTemplate = CouponTemplateDummy.dummy(couponPolicy, couponType, product,
                category, couponStateCode);

        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(category);
        entityManager.persist(couponStateCode);
        entityManager.persist(tier);
        entityManager.persist(couponTemplate);

        tierCoupon = TierCouponDummy.dummy(couponTemplate, tier);

        entityManager.persist(tierCoupon);
    }

    @Test
    @DisplayName("등급 쿠폰 save 테스트")
    void tierCouponSaveTest() {
        TierCoupon persist = tierCouponRepository.save(tierCoupon);

        Optional<TierCoupon> result = tierCouponRepository.findById(persist.getPk());

        assertThat(result).isPresent();
        assertThat(result.get().getCouponTemplate().getTemplateNo()).isEqualTo(
                persist.getCouponTemplate().getTemplateNo());
        assertThat(result.get().getBookPubTier().getTierNo()).isEqualTo(
                persist.getBookPubTier().getTierNo());
    }


    @Test
    @DisplayName("등급 쿠폰 전체 조회 테스트")
    void findTierCouponsTest() {

        Pageable pageable = Pageable.ofSize(10);
        Page<GetTierCouponResponseDto> result = tierCouponRepository.findAllBy(pageable);
        List<GetTierCouponResponseDto> content = result.getContent();

        assertThat(result).isNotEmpty();
        assertThat(content.get(0).getTierNo()).isEqualTo(tierCoupon.getPk().getTierNo());
        assertThat(content.get(0).getTierName()).isEqualTo(
                tierCoupon.getBookPubTier().getTierName());
        assertThat(content.get(0).getTemplateNo()).isEqualTo(
                tierCoupon.getCouponTemplate().getTemplateNo());
        assertThat(content.get(0).getTemplateName()).isEqualTo(
                tierCoupon.getCouponTemplate().getTemplateName());
    }

    @Test
    @DisplayName("등급 번호로 쿠폰 리스트 조회 테스트")
    void findTierCouponByTierNoTest() {

        List<Long> result = tierCouponRepository.findAllByTierNo(tier.getTierNo());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isEqualTo(tierCoupon.getPk().getCouponTemplateNo());
    }

}