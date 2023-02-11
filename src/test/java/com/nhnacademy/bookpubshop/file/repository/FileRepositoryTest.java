package com.nhnacademy.bookpubshop.file.repository;

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
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
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

/**
 * FileRepoTest
 *
 * @author : 유호철
 * @since : 1.0
 **/
@DataJpaTest
class FileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FileRepository fileRepository;

    File file;

    Member member;

    Product product;

    BookpubOrder order;

    CouponTemplate couponTemplate;

    PricePolicy packagingPrice;

    PricePolicy deliveryPrice;

    Review review;

    ReviewPolicy reviewPolicy;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    OrderStateCode orderStateCode;
    BookPubTier tier;
    CouponStateCode couponStateCodeDummy;
    Category category;
    CouponPolicy couponPolicy;
    CustomerServiceStateCode customerServiceStateCode;
    CustomerService customerService;
    InquiryStateCode inquiryStateCode;
    Inquiry inquiry;
    CouponType couponType;

    @BeforeEach
    void setUp() {
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        deliveryPrice = PricePolicyDummy.dummy();
        packagingPrice = PricePolicyDummy.dummy();
        orderStateCode = OrderStateCodeDummy.dummy();
        order = OrderDummy.dummy(member, deliveryPrice, packagingPrice, orderStateCode);
        couponStateCodeDummy = CouponStateCodeDummy.dummy();
        category = CategoryDummy.dummy();
        couponPolicy = CouponPolicyDummy.dummy();
        couponType = CouponTypeDummy.dummy();

        couponTemplate = CouponTemplateDummy.dummy(couponPolicy, couponType, product, category, couponStateCodeDummy);
        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        reviewPolicy = ReviewPolicyDummy.dummy();
        review = ReviewDummy.dummy(member, product, reviewPolicy);
        customerServiceStateCode = CustomerServiceStateCodeDummy.dummy();
        customerService = CustomerServiceDummy.dummy(customerServiceStateCode, member);
        inquiryStateCode = InquiryCodeDummy.dummy();
        inquiry = InquiryDummy.dummy(null, member, product, inquiryStateCode);

        file = FileDummy.dummy(inquiry, review, couponTemplate, product, customerService, null);

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
        entityManager.persist(deliveryPrice);
        entityManager.persist(packagingPrice);
        entityManager.persist(orderStateCode);
        entityManager.persist(order);
        entityManager.persist(couponStateCodeDummy);
        entityManager.persist(category);
        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(couponTemplate);
        entityManager.persist(tier);
        entityManager.persist(member);
        entityManager.persist(reviewPolicy);
        entityManager.persist(review);
        entityManager.persist(customerServiceStateCode);
        entityManager.persist(customerService);
        entityManager.persist(inquiryStateCode);
        entityManager.persist(inquiry);
    }

    @DisplayName("파일 Repo save 테스트")
    @Test
    void fileRepositorySaveTest() {
        LocalDateTime now = LocalDateTime.now();
        entityManager.persist(review.getReviewPolicy());
        File persist = entityManager.persist(file);

        Optional<File> result = fileRepository.findById(persist.getFileNo());
        assertThat(result).isPresent();
        assertThat(result.get().getFileNo()).isEqualTo(persist.getFileNo());
        assertThat(result.get().getFileCategory()).isEqualTo(persist.getFileCategory());
        assertThat(result.get().getFilePath()).isEqualTo(persist.getFilePath());
        assertThat(result.get().getFileExtension()).isEqualTo(persist.getFileExtension());
        assertThat(result.get().getNameSaved()).isEqualTo(persist.getNameSaved());
        assertThat(result.get().getInquiry().getInquiryNo())
                .isEqualTo(persist.getInquiry().getInquiryNo());
        assertThat(result.get().getCustomerService().getServiceNo())
                .isEqualTo(persist.getCustomerService().getServiceNo());
        assertThat(result.get().getProduct().getProductNo()).isEqualTo(persist.getProduct().getProductNo());
        assertThat(result.get().getFileNo()).isEqualTo(persist.getFileNo());
        assertThat(result.get().getReview()).isEqualTo(persist.getReview());
        assertThat(result.get().getCouponTemplate()).isEqualTo(persist.getCouponTemplate());
        assertThat(result.get().getNameOrigin()).isEqualTo(persist.getNameOrigin());

        assertThat(result.get().getCreatedAt()).isAfter(now);
    }
}