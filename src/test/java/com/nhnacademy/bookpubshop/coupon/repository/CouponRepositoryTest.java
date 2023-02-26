package com.nhnacademy.bookpubshop.coupon.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetOrderCouponResponseDto;
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
import com.nhnacademy.bookpubshop.inquiry.dummy.InquiryDummy;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.inquirystatecode.dummy.InquiryCodeDummy;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
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
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

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
    CouponPolicy couponPolicy;
    CouponType couponType;
    Category category;
    CouponStateCode couponStateCode;
    CouponTemplate couponTemplate;
    Coupon coupon;
    Inquiry inquiry;
    Review review;
    ReviewPolicy reviewPolicy;
    CustomerService customerService;
    CustomerServiceStateCode customerServiceStateCode;
    InquiryStateCode inquiryStateCode;
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
        order = new BookpubOrder(
                null,
                member,
                new PricePolicy(null, "배송비", 3000L),
                new PricePolicy(null, "포장비", 1500L),
                new OrderStateCode(null, OrderState.COMPLETE_DELIVERY.getName(), OrderState.COMPLETE_DELIVERY.isUsed(), "배송완료"),
                "test_recipient",
                "test_recipient_phone",
                "test_buyer",
                "test_buyer_phone",
                LocalDateTime.now(),
                null,
                10000L,
                100L,
                1500L,
                true,
                null,
                1000L,
                "IT",
                "광주 동구 조선대길",
                "asdkjasl",
                "orderName"
        );

        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        orderProductStateCode = new OrderProductStateCode(null,
                OrderProductState.CONFIRMED.getName(),
                OrderProductState.CONFIRMED.isUsed(),
                "주문완료되었습니다.");

        bookPubTier = TierDummy.dummy();
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
                orderStateCode,
                "test_recipient",
                "test_recipient_phone",
                "test_buyer",
                "test_buyer_phone",
                LocalDateTime.now(),
                null,
                10000L,
                100L,
                1500L,
                true,
                null,
                1000L,
                "IT",
                "광주 동구 조선대길",
                "askdjkjvhvz",
                "orderName"
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
                "reason",
                100L,
                ""
        );

        coupon = CouponDummy.dummy(
                couponTemplate,
                order,
                orderProduct,
                member
        );
        inquiryStateCode = InquiryCodeDummy.dummy();
        inquiry = InquiryDummy.dummy(null, member, product, inquiryStateCode);
        reviewPolicy = ReviewPolicyDummy.dummy();
        review = ReviewDummy.dummy(member, product, reviewPolicy);
        customerServiceStateCode = CustomerServiceStateCodeDummy.dummy();
        customerService = CustomerServiceDummy.dummy(customerServiceStateCode, member);


        file = FileDummy.dummy(inquiry, review, couponTemplate, product, customerService, null);
        couponTemplate.setFile(file);


        entityManager.persist(bookPubTier);
        entityManager.persist(member);
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
        entityManager.persist(inquiryStateCode);
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
        Optional<GetCouponResponseDto> result = couponRepository.findByCouponNo(persist.getCouponNo());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getCouponNo()).isEqualTo(persist.getCouponNo());
        assertThat(result.get().getMemberId()).isEqualTo(persist.getMember().getMemberId());
        assertThat(result.get().getTemplateName()).isEqualTo(persist.getCouponTemplate().getTemplateName());
        assertThat(result.get().getTemplateImage()).isEqualTo(persist.getCouponTemplate().getFile().getFilePath());
        assertThat(result.get().isPolicyFixed()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().isPolicyFixed());
        assertThat(result.get().getPolicyPrice()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyPrice());
        assertThat(result.get().getPolicyMinimum()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyMinimum());
        assertThat(result.get().getMaxDiscount()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getMaxDiscount());
        assertThat(result.get().getFinishedAt()).isEqualTo(persist.getCouponTemplate().getFinishedAt());
        assertThat(result.get().isCouponUsed()).isEqualTo(persist.isCouponUsed());
    }

    @Test
    @DisplayName("쿠폰 리스트 페이지를 조회하는 테스트_쿠폰이름으로 검색할 경우")
    void getCoupons_Test_SearchTemplateName() {
        // given
        Coupon persist = entityManager.persist(coupon);
        Pageable pageable = Pageable.ofSize(10);

        // when
        Page<GetCouponResponseDto> page = couponRepository.findAllBy(pageable, "templateName", persist.getCouponTemplate().getTemplateName());

        // then
        assertThat(page).isNotEmpty();
        assertThat(page.getContent().get(0).getCouponNo()).isEqualTo(persist.getCouponNo());
        assertThat(page.getContent().get(0).getMemberId()).isEqualTo(persist.getMember().getMemberId());
        assertThat(page.getContent().get(0).getTemplateName()).isEqualTo(persist.getCouponTemplate().getTemplateName());
        assertThat(page.getContent().get(0).getTemplateImage()).isEqualTo(persist.getCouponTemplate().getFile().getFilePath());
        assertThat(page.getContent().get(0).isPolicyFixed()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().isPolicyFixed());
        assertThat(page.getContent().get(0).getPolicyPrice()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyPrice());
        assertThat(page.getContent().get(0).getPolicyMinimum()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyMinimum());
        assertThat(page.getContent().get(0).getMaxDiscount()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getMaxDiscount());
        assertThat(page.getContent().get(0).getFinishedAt()).isEqualTo(persist.getCouponTemplate().getFinishedAt());
        assertThat(page.getContent().get(0).isCouponUsed()).isEqualTo(persist.isCouponUsed());
    }

    @Test
    @DisplayName("쿠폰 리스트 페이지를 조회하는 테스트_쿠폰 번호로 검색할 경우")
    void getCoupons_Test_SearchCouponNo() {
        // given
        Coupon persist = entityManager.persist(coupon);
        Pageable pageable = Pageable.ofSize(10);

        // when
        Page<GetCouponResponseDto> page = couponRepository.findAllBy(pageable, "couponNo", String.valueOf(persist.getCouponNo()));

        // then
        assertThat(page).isNotEmpty();
        assertThat(page.getContent().get(0).getCouponNo()).isEqualTo(persist.getCouponNo());
        assertThat(page.getContent().get(0).getMemberId()).isEqualTo(persist.getMember().getMemberId());
        assertThat(page.getContent().get(0).getTemplateName()).isEqualTo(persist.getCouponTemplate().getTemplateName());
        assertThat(page.getContent().get(0).getTemplateImage()).isEqualTo(persist.getCouponTemplate().getFile().getFilePath());
        assertThat(page.getContent().get(0).isPolicyFixed()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().isPolicyFixed());
        assertThat(page.getContent().get(0).getPolicyPrice()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyPrice());
        assertThat(page.getContent().get(0).getPolicyMinimum()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyMinimum());
        assertThat(page.getContent().get(0).getMaxDiscount()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getMaxDiscount());
        assertThat(page.getContent().get(0).getFinishedAt()).isEqualTo(persist.getCouponTemplate().getFinishedAt());
        assertThat(page.getContent().get(0).isCouponUsed()).isEqualTo(persist.isCouponUsed());
    }

    @Test
    @DisplayName("쿠폰 리스트 페이지를 조회하는 테스트_멤버 아이디로 검색할 경우")
    void getCoupons_Test_SearchMemberId() {
        // given
        Coupon persist = entityManager.persist(coupon);
        Pageable pageable = Pageable.ofSize(10);

        // when
        Page<GetCouponResponseDto> page = couponRepository.findAllBy(pageable, "memberId", persist.getMember().getMemberId());

        // then
        assertThat(page).isNotEmpty();
        assertThat(page.getContent().get(0).getCouponNo()).isEqualTo(persist.getCouponNo());
        assertThat(page.getContent().get(0).getMemberId()).isEqualTo(persist.getMember().getMemberId());
        assertThat(page.getContent().get(0).getTemplateName()).isEqualTo(persist.getCouponTemplate().getTemplateName());
        assertThat(page.getContent().get(0).getTemplateImage()).isEqualTo(persist.getCouponTemplate().getFile().getFilePath());
        assertThat(page.getContent().get(0).isPolicyFixed()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().isPolicyFixed());
        assertThat(page.getContent().get(0).getPolicyPrice()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyPrice());
        assertThat(page.getContent().get(0).getPolicyMinimum()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyMinimum());
        assertThat(page.getContent().get(0).getMaxDiscount()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getMaxDiscount());
        assertThat(page.getContent().get(0).getFinishedAt()).isEqualTo(persist.getCouponTemplate().getFinishedAt());
        assertThat(page.getContent().get(0).isCouponUsed()).isEqualTo(persist.isCouponUsed());
    }

    @Test
    @DisplayName("쿠폰 리스트 페이지를 조회하는 테스트_바르지 않은 검색어일 경우")
    void getCoupons_Test_SearchWeird() {
        // given
        Coupon persist = entityManager.persist(coupon);
        Pageable pageable = Pageable.ofSize(10);

        // when
        Page<GetCouponResponseDto> page = couponRepository.findAllBy(pageable, "asdf", null);

        // then
        assertThat(page).isNotEmpty();
        assertThat(page.getContent().get(0).getCouponNo()).isEqualTo(persist.getCouponNo());
        assertThat(page.getContent().get(0).getMemberId()).isEqualTo(persist.getMember().getMemberId());
        assertThat(page.getContent().get(0).getTemplateName()).isEqualTo(persist.getCouponTemplate().getTemplateName());
        assertThat(page.getContent().get(0).getTemplateImage()).isEqualTo(persist.getCouponTemplate().getFile().getFilePath());
        assertThat(page.getContent().get(0).isPolicyFixed()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().isPolicyFixed());
        assertThat(page.getContent().get(0).getPolicyPrice()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyPrice());
        assertThat(page.getContent().get(0).getPolicyMinimum()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyMinimum());
        assertThat(page.getContent().get(0).getMaxDiscount()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getMaxDiscount());
        assertThat(page.getContent().get(0).getFinishedAt()).isEqualTo(persist.getCouponTemplate().getFinishedAt());
        assertThat(page.getContent().get(0).isCouponUsed()).isEqualTo(persist.isCouponUsed());
    }

    @Test
    @DisplayName("쿠폰 리스트 페이지를 조회하는 테스트_조건이 없을 경우")
    void getCoupons_Test() {
        // given
        Coupon persist = entityManager.persist(coupon);
        Pageable pageable = Pageable.ofSize(10);

        // when
        Page<GetCouponResponseDto> page = couponRepository.findAllBy(pageable, null, null);

        // then
        assertThat(page).isNotEmpty();
        assertThat(page.getContent().get(0).getCouponNo()).isEqualTo(persist.getCouponNo());
        assertThat(page.getContent().get(0).getMemberId()).isEqualTo(persist.getMember().getMemberId());
        assertThat(page.getContent().get(0).getTemplateName()).isEqualTo(persist.getCouponTemplate().getTemplateName());
        assertThat(page.getContent().get(0).getTemplateImage()).isEqualTo(persist.getCouponTemplate().getFile().getFilePath());
        assertThat(page.getContent().get(0).isPolicyFixed()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().isPolicyFixed());
        assertThat(page.getContent().get(0).getPolicyPrice()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyPrice());
        assertThat(page.getContent().get(0).getPolicyMinimum()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyMinimum());
        assertThat(page.getContent().get(0).getMaxDiscount()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getMaxDiscount());
        assertThat(page.getContent().get(0).getFinishedAt()).isEqualTo(persist.getCouponTemplate().getFinishedAt());
        assertThat(page.getContent().get(0).isCouponUsed()).isEqualTo(persist.isCouponUsed());
    }

    @Test
    @DisplayName("주문 시 사용 가능한 쿠폰 조회 테스트")
    void findByProductNoTest() {
        //given
        Coupon memberCoupon = CouponDummy.dummy(couponTemplate, null, null, member);
        Coupon persist = entityManager.persist(memberCoupon);

        //when
        List<GetOrderCouponResponseDto> couponList = couponRepository.findByProductNo(member.getMemberNo(), product.getProductNo());

        //then
        assertThat(couponList.get(0).getCouponNo()).isEqualTo(persist.getCouponNo());
        assertThat(couponList.get(0).getTemplateName()).isEqualTo(persist.getCouponTemplate().getTemplateName());
        assertThat(couponList.get(0).getProductNo()).isEqualTo(persist.getCouponTemplate().getProduct().getProductNo());
        assertThat(couponList.get(0).getCategoryNo()).isEqualTo(persist.getCouponTemplate().getCategory().getCategoryNo());
        assertThat(couponList.get(0).getPolicyPrice()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyPrice());
        assertThat(couponList.get(0).isPolicyFixed()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().isPolicyFixed());
        assertThat(couponList.get(0).getPolicyMinimum()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getPolicyMinimum());
        assertThat(couponList.get(0).getMaxDiscount()).isEqualTo(persist.getCouponTemplate().getCouponPolicy().getMaxDiscount());
        assertThat(couponList.get(0).isTemplateBundled()).isEqualTo(persist.getCouponTemplate().isTemplateBundled());
    }

    private Category category() {
        Category category = new Category(null, null, "name",
                0, true);

        return entityManager.persist(category);
    }

    @DisplayName("월별 쿠폰이 존재하는지 확인")
    @Test
    void existsMonthCoupon() {
        //given
        entityManager.persist(coupon);

        boolean result = couponRepository.existsMonthCoupon(member.getMemberNo(), couponTemplate.getTemplateNo());
        assertThat(result).isTrue();
    }

    @DisplayName("이달의 쿠폰들이 존재하는지 확인")
    @Test
    void existsMonthCouponList() {
        entityManager.persist(coupon);

        List<Long> result = couponRepository.existsMonthCouponList(member.getMemberNo(), List.of(couponTemplate.getTemplateNo()));

        assertThat(result).isNotEmpty();
    }

    @DisplayName("주문번호를 통해 쿠폰을 확인")
    @Test
    void findByCouponByOrderNoTest() {
        ReflectionTestUtils.setField(coupon, "couponUsed", true);

        entityManager.persist(coupon);

        List<Coupon> result = couponRepository.findByCouponByOrderNo(order.getOrderNo());


        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCouponNo()).isEqualTo(coupon.getCouponNo());
        assertThat(result.get(0).getMember()).isEqualTo(member);
        assertThat(result.get(0).getCouponTemplate()).isEqualTo(couponTemplate);
        assertThat(result.get(0).getOrder()).isEqualTo(order);
        assertThat(result.get(0).getOrderProduct()).isEqualTo(orderProduct);
        assertThat(result.get(0).isCouponUsed()).isEqualTo(coupon.isCouponUsed());
        assertThat(result.get(0).getUsedAt()).isEqualTo(coupon.getUsedAt());
    }

    @DisplayName("주문번호를 통해 사용한 쿠폰을 조회하는 테스트")
    @Test
    void findByCouponByOrderProductNoTest() {
        ReflectionTestUtils.setField(coupon, "couponUsed", true);
        entityManager.persist(coupon);

        List<Coupon> result = couponRepository.findByCouponByOrderProductNo(orderProduct.getOrderProductNo());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCouponNo()).isEqualTo(coupon.getCouponNo());
        assertThat(result.get(0).getCouponTemplate()).isEqualTo(coupon.getCouponTemplate());
        assertThat(result.get(0).getOrder()).isEqualTo(coupon.getOrder());
        assertThat(result.get(0).getOrderProduct()).isEqualTo(coupon.getOrderProduct());
        assertThat(result.get(0).getMember()).isEqualTo(coupon.getMember());
        assertThat(result.get(0).isCouponUsed()).isEqualTo(coupon.isCouponUsed());
        assertThat(result.get(0).getUsedAt()).isEqualTo(coupon.getUsedAt());
    }

    @DisplayName("회원번호를 통해 사용가능한 쿠폰을 받는 테스트")
    @Test
    void findPositiveCouponByMemberNoTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        entityManager.persist(coupon);

        Page<GetCouponResponseDto> page = couponRepository.findPositiveCouponByMemberNo(pageRequest, member.getMemberNo());
        List<GetCouponResponseDto> result = page.getContent();

        assertThat(page).isNotEmpty();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCouponNo()).isEqualTo(coupon.getCouponNo());
        assertThat(result.get(0).getMemberId()).isEqualTo(member.getMemberId());
        assertThat(result.get(0).getTemplateName()).isEqualTo(couponTemplate.getTemplateName());
        assertThat(result.get(0).getTemplateImage()).isEqualTo(file.getFilePath());
        assertThat(result.get(0).getTypeName()).isEqualTo(couponType.getTypeName());
        assertThat(result.get(0).isPolicyFixed()).isEqualTo(couponPolicy.isPolicyFixed());
        assertThat(result.get(0).getPolicyPrice()).isEqualTo(couponPolicy.getPolicyPrice());
        assertThat(result.get(0).getPolicyMinimum()).isEqualTo(couponPolicy.getPolicyMinimum());
        assertThat(result.get(0).getMaxDiscount()).isEqualTo(couponPolicy.getMaxDiscount());
        assertThat(result.get(0).getFinishedAt()).isEqualTo(couponTemplate.getFinishedAt());
        assertThat(result.get(0).isCouponUsed()).isEqualTo(coupon.isCouponUsed());
    }

    @DisplayName("회원번호를 통해 사용한 쿠폰을 받는 테스트")
    @Test
    void findNegativeCouponByMemberNo() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        ReflectionTestUtils.setField(coupon, "couponUsed", true);
        entityManager.persist(coupon);

        Page<GetCouponResponseDto> page = couponRepository.findNegativeCouponByMemberNo(pageRequest, member.getMemberNo());
        List<GetCouponResponseDto> result = page.getContent();

        assertThat(page).isNotEmpty();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCouponNo()).isEqualTo(coupon.getCouponNo());
        assertThat(result.get(0).getMemberId()).isEqualTo(member.getMemberId());
        assertThat(result.get(0).getTemplateName()).isEqualTo(couponTemplate.getTemplateName());
        assertThat(result.get(0).getTemplateImage()).isEqualTo(file.getFilePath());
        assertThat(result.get(0).getTypeName()).isEqualTo(couponType.getTypeName());
        assertThat(result.get(0).isPolicyFixed()).isEqualTo(couponPolicy.isPolicyFixed());
        assertThat(result.get(0).getPolicyPrice()).isEqualTo(couponPolicy.getPolicyPrice());
        assertThat(result.get(0).getPolicyMinimum()).isEqualTo(couponPolicy.getPolicyMinimum());
        assertThat(result.get(0).getMaxDiscount()).isEqualTo(couponPolicy.getMaxDiscount());
        assertThat(result.get(0).getFinishedAt()).isEqualTo(couponTemplate.getFinishedAt());
        assertThat(result.get(0).isCouponUsed()).isEqualTo(coupon.isCouponUsed());
    }

    @DisplayName("등급 쿠폰을 사용가능했는지 확인하기위한 테스트")
    @Test
    void existsTierCouponsByMemberNoTest(){
        entityManager.persist(coupon);

        boolean result = couponRepository.existsTierCouponsByMemberNo(member.getMemberNo(),
                List.of(couponTemplate.getTemplateNo()));

        assertThat(result).isTrue();
    }
}
