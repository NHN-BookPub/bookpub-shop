package com.nhnacademy.bookpubshop.coupon.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
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
import com.nhnacademy.bookpubshop.customersupport.dummy.CustomerServiceDummy;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.personalinquiry.dummy.PersonalInquiryDummy;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.review.dummy.ReviewDummy;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.reviewpolicy.dummy.ReviewPolicyDummy;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import com.nhnacademy.bookpubshop.servicecode.dummy.CustomerServiceStateCodeDummy;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.state.OrderState;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
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
    PersonalInquiry inquiry;
    Review review;
    ReviewPolicy reviewPolicy;
    CustomerService customerService;
    CustomerServiceStateCode customerServiceStateCode;
    File file;
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

        inquiry = PersonalInquiryDummy.dummy(member);
        reviewPolicy = ReviewPolicyDummy.dummy();
        review = ReviewDummy.dummy(member, product, reviewPolicy);
        customerServiceStateCode = CustomerServiceStateCodeDummy.dummy();
        customerService = CustomerServiceDummy.dummy(customerServiceStateCode, member);


        file = FileDummy.dummy(inquiry, review, couponTemplate, product, customerService);


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
        entityManager.persist(inquiry);
        entityManager.persist(reviewPolicy);
        entityManager.persist(review);
        entityManager.persist(customerServiceStateCode);
        entityManager.persist(customerService);
        entityManager.persist(file);
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

    @Test
    @DisplayName("쿠폰 번호를 통해 쿠폰을 조회하는 테스트")
    void getCoupon_Test() {
        // given
        Coupon persist = entityManager.persist(coupon);

        // when
        Optional<GetCouponResponseDto> result = couponRepository.getCoupon(persist.getCouponNo());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getCouponNo()).isEqualTo(persist.getCouponNo());
        assertThat(result.get().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(result.get().getTemplateName()).isEqualTo(couponTemplate.getTemplateName());
        assertThat(result.get().getTemplateImage()).isEqualTo(file.getNameSaved().concat(file.getFileExtension()));
        assertThat(result.get().isPolicyFixed()).isEqualTo(couponPolicy.isPolicyFixed());
        assertThat(result.get().getPolicyPrice()).isEqualTo(couponPolicy.getPolicyPrice());
        assertThat(result.get().getPolicyMinimum()).isEqualTo(couponPolicy.getPolicyMinimum());
        assertThat(result.get().getMaxDiscount()).isEqualTo(couponPolicy.getMaxDiscount());
        assertThat(result.get().getFinishedAt()).isEqualTo(couponTemplate.getFinishedAt());
        assertThat(result.get().isCouponUsed()).isEqualTo(coupon.isCouponUsed());
    }

    @Test
    @DisplayName("쿠폰 리스트 페이지를 조하는 테스트")
    void getCoupons_Test() {
        // given
        entityManager.persist(coupon);
        GetCouponResponseDto dto = new GetCouponResponseDto(1L, "memberId", "templateName", "Image", true, 1L, 10L, 100L, LocalDateTime.now(), false);
        Pageable pageable = Pageable.ofSize(10);

        // when
        Page<GetCouponResponseDto> page = couponRepository.getCoupons(pageable);

        // then
        assertThat(page).isNotEmpty();
        assertThat(page.getContent().get(0).getCouponNo()).isEqualTo(coupon.getCouponNo());
        assertThat(page.getContent().get(0).getMemberId()).isEqualTo(member.getMemberId());
        assertThat(page.getContent().get(0).getTemplateName()).isEqualTo(couponTemplate.getTemplateName());
        assertThat(page.getContent().get(0).getTemplateImage()).isEqualTo(file.getNameSaved().concat(file.getFileExtension()));
        assertThat(page.getContent().get(0).isPolicyFixed()).isEqualTo(couponPolicy.isPolicyFixed());
        assertThat(page.getContent().get(0).getPolicyPrice()).isEqualTo(couponPolicy.getPolicyPrice());
        assertThat(page.getContent().get(0).getPolicyMinimum()).isEqualTo(couponPolicy.getPolicyMinimum());
        assertThat(page.getContent().get(0).getMaxDiscount()).isEqualTo(couponPolicy.getMaxDiscount());
        assertThat(page.getContent().get(0).getFinishedAt()).isEqualTo(couponTemplate.getFinishedAt());
        assertThat(page.getContent().get(0).isCouponUsed()).isEqualTo(coupon.isCouponUsed());
    }

    private Category category() {
        Category category = new Category(null, null, "name",
                0, true);

        return entityManager.persist(category);
    }
}
