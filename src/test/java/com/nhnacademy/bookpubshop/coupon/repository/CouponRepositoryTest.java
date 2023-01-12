package com.nhnacademy.bookpubshop.coupon.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.coupon.dummy.CouponDummy;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dummy.CouponTemplateDummy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.state.OrderState;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 쿠폰 레포지토리 테스트
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class CouponRepositoryTest {
    @Autowired
    CouponRepository couponRepository;

    @Autowired
    TestEntityManager entityManager;

    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    Product product;
    OrderProductStateCode orderProductStateCode;
    PricePolicy deliveryPricePolicy;
    PricePolicy packagePricePolicy;
    BookPubTier bookPubTier;
    Member member;
    BookpubOrder order;
    OrderStateCode orderStateCode;
    Address address;
    CouponPolicy couponPolicy;
    CouponType couponType;
    Category category;
    CouponStateCode couponStateCode;
    CouponTemplate couponTemplate;
    Coupon coupon;

    OrderProduct orderProduct;

    @BeforeEach
    void setUp() {
        orderProductStateCode =
                new OrderProductStateCode(null,
                        OrderProductState.CONFIRMED.getName(),
                        OrderProductState.CONFIRMED.isUsed(),
                        "주문완료되었습니다.");

        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        address = AddressDummy.dummy();
        order = new BookpubOrder(
                null,
                member,
                new PricePolicy(null, "배송비", 3000L),
                new PricePolicy(null, "포장비", 1500L),
                address,
                new OrderStateCode(null, OrderState.COMPLETE_DELIVERY.getName(), OrderState.COMPLETE_DELIVERY.isUsed(), "배송완료"),
                "test_recipient",
                "test_recipient_phone",
                "test_buyer",
                "test_buyer_phone",
                LocalDateTime.now(),
                null,
                10000L,
                100L,
                true,
                null,
                1000L
        );

        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        orderProductStateCode = new OrderProductStateCode(null,
                OrderProductState.CONFIRMED.getName(),
                OrderProductState.CONFIRMED.isUsed(),
                "주문완료되었습니다.");

        bookPubTier = new BookPubTier(null, "브론즈");

        member = MemberDummy.dummy(bookPubTier);

        orderStateCode = new OrderStateCode(
                null,
                OrderState.COMPLETE_DELIVERY.getName(),
                OrderState.COMPLETE_DELIVERY.isUsed(),
                "배송완료");

        packagePricePolicy = new PricePolicy(null, "포장비", 1500L);
        deliveryPricePolicy = new PricePolicy(null, "배송비", 3000L);

        order = new BookpubOrder(
                null,
                member,
                deliveryPricePolicy,
                packagePricePolicy,
                address,
                orderStateCode,
                "test_recipient",
                "test_recipient_phone",
                "test_buyer",
                "test_buyer_phone",
                LocalDateTime.now(),
                null,
                10000L,
                100L,
                true,
                null,
                1000L
        );

        couponStateCode = CouponStateCodeDummy.dummy();
        couponPolicy = CouponPolicyDummy.dummy();
        couponType = CouponTypeDummy.dummy();
        category = category();
        couponTemplate = CouponTemplateDummy.dummy(
                couponPolicy,
                couponType,
                product,
                category,
                couponStateCode
        );

        orderProduct = new OrderProduct(
                null,
                product,
                order,
                orderProductStateCode,
                10,
                1000L,
                100000L,
                "reason"
        );

        coupon = CouponDummy.dummy(
                couponTemplate,
                order,
                orderProduct,
                member
        );

        entityManager.persist(bookPubTier);
        entityManager.persist(member);
        entityManager.persist(address);
        entityManager.persist(productPolicy);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(deliveryPricePolicy);
        entityManager.persist(packagePricePolicy);
        entityManager.persist(product);
        entityManager.persist(orderProductStateCode);
        entityManager.persist(orderStateCode);
        entityManager.persist(couponPolicy);
        entityManager.persist(couponStateCode);
        entityManager.persist(couponType);
        entityManager.persist(category);
        entityManager.persist(order);
        entityManager.persist(orderProduct);
        entityManager.persist(couponTemplate);
    }

    @Test
    @DisplayName("쿠폰 레포지토리 테스트")
    void CouponSaveTest() {
        Coupon persist = entityManager.persist(coupon);

        Optional<Coupon> findCoupon = couponRepository.findById(persist.getCouponNo());

        assertThat(findCoupon).isPresent();
        assertThat(findCoupon.get().getCouponTemplate().getTemplateName()).isEqualTo(persist.getCouponTemplate().getTemplateName());
        assertThat(findCoupon.get().getCouponNo()).isEqualTo(persist.getCouponNo());
        assertThat(findCoupon.get().getOrder().getOrderNo()).isEqualTo(persist.getOrder().getOrderNo());
        assertThat(findCoupon.get().getOrderProduct().getOrder().getOrderNo()).isEqualTo(persist.getOrderProduct().getOrder().getOrderNo());
        assertThat(findCoupon.get().getMember().getMemberNo()).isEqualTo(persist.getMember().getMemberNo());
        assertThat(findCoupon.get().isCouponUsed()).isFalse();
        assertThat(findCoupon.get().getUsedAt()).isEqualTo(persist.getUsedAt());
    }

    private Category category() {
        Category category = new Category(null, null, "name",
                0, true);

        return entityManager.persist(category);
    }
}